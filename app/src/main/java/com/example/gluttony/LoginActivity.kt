package com.example.gluttony

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gluttony.databinding.ActivityLoginBinding
import com.example.gluttony.models.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignIn.*
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class LoginActivity : AppCompatActivity() {
    private lateinit var email :String
    private lateinit var password : String
    private val userName: String? = null
    private lateinit var auth : FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var binding: ActivityLoginBinding
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        //Initialization of Variables
        auth = Firebase.auth
        database = Firebase.database.reference
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        binding.btnLogin.setOnClickListener{
            email = binding.editTextTextEmailAddress.text.toString().trim()
            password = binding.editTextTextPassword.text.toString().trim()
            if(email.isBlank()|| password.isBlank()){
                Toast.makeText(this, "Fill All The Fields First!", Toast.LENGTH_SHORT).show()
            }else{
                createAccount(email, password)
            }
        }
        binding.btnHaveAccount.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.btnGoogle.setOnClickListener(){
            val googleIntent = googleSignInClient.signInIntent
            launcher.launch(googleIntent)
        }
    }
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result->
        if(result.resultCode == Activity.RESULT_OK){
            val task = getSignedInAccountFromIntent(result.data)
            if(task.isSuccessful){
                val account : GoogleSignInAccount = task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener{
                    authTask->
                    if(authTask.isSuccessful){
                        Toast.makeText(this, "Successfully Sign_In With Google", Toast.LENGTH_SHORT).show()
                        updateUi(authTask.result?.user)
                        finish()
                    }else{
                        Toast.makeText(this, "Google Sign_In Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }else{
            Toast.makeText(this, "Google Sign_In Failed", Toast.LENGTH_SHORT).show()
        }
    }
    private fun createAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{task->
            if(task.isSuccessful){
                val user = auth.currentUser
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                updateUi(user)
            }else{
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{task->
                    if(task.isSuccessful){
                        val user = auth.currentUser
                        Toast.makeText(this, "Account Creation & Login Successfully!", Toast.LENGTH_SHORT).show()
                        saveUserData()
                        updateUi(user)
                    }else {
                        Toast.makeText(this, "Authentication Failed! ", Toast.LENGTH_SHORT).show()
                        Log.d(
                            "Account",
                            "createUserAccount : Authentication Failed",
                            task.exception
                        )
                    }
                }
            }
        }

    }

    private fun saveUserData() {
        email = binding.editTextTextEmailAddress.text.toString().trim()
        password = binding.editTextTextPassword.text.toString().trim()
        val user = UserModel(userName, password, email)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        database.child("user").child(userId).setValue(user)
    }

    private fun updateUi(user: FirebaseUser?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser !=null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}