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
import com.example.gluttony.databinding.ActivitySignUpBinding
import com.example.gluttony.models.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Variables
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        auth = Firebase.auth
        database = Firebase.database.reference
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        //ClickListener
        binding.alreadyHaveAccountTv.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        //ClickListner
        binding.btnSignUp.setOnClickListener{
            userName = binding.signUpName.text.toString().trim()
            password = binding.signUpPassword.text.toString().trim()
            email = binding.signUpEMail.text.toString().trim()
            if(userName.isBlank()|| password.isBlank()|| email.isBlank()){
                Toast.makeText(this, "Fill All The Details First!", Toast.LENGTH_SHORT).show()
            }
            else {
                createAccount(email,password)
            }
        }
        binding.signUpBtnGoogle.setOnClickListener(){
            val signIntent = googleSignInClient.signInIntent
            launcher.launch(signIntent)
        }
    }
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if(task.isSuccessful){
                val account: GoogleSignInAccount = task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener{
                        task->
                    if(task.isSuccessful){
                        Toast.makeText(this,"Successfully Sign_In With Google", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    else{
                        Toast.makeText(this, "Google Sign_In Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        else {
            Toast.makeText(this, "Google Sign_In Failed", Toast.LENGTH_SHORT).show()
        }
    }
    //create Account
    private fun createAccount(email: String, password: String) {
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{task->

                if(task.isSuccessful){
                    Toast.makeText(this, "Account Created Successful!", Toast.LENGTH_SHORT).show()
                    saveUserData()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this, "Account Creating Failed!", Toast.LENGTH_SHORT).show()
                    Log.d("Account", "createAccount: Failure", task.exception)
                }
            }
    }
    //Save Data to Firebase
    private fun saveUserData() {
        userName  = binding.signUpName.text.toString()
        password = binding.signUpPassword.text.toString().trim()
        email = binding.signUpEMail.text.toString().trim()
        val user = UserModel(userName,password, email)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        database.child("user").child(userId).setValue(user)
    }
}