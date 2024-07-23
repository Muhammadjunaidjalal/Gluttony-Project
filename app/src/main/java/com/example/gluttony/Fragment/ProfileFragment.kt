package com.example.gluttony.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.gluttony.R
import com.example.gluttony.databinding.FragmentProfileBinding
import com.example.gluttony.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val auth  = FirebaseAuth.getInstance()
    private val databaseReference = FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        setUserData()
        binding.SaveInformationButton.setOnClickListener(){
            val name = binding.Name.text.toString()
            val address = binding.Address.text.toString()
            val email = binding.Email.text.toString()
            val phone = binding.Phone.text.toString()

            updateUserData(name,address,email,phone)
        }
        return binding.root
    }

    private fun updateUserData(name: String, address: String, email: String, phone: String) {
        val userId = auth.currentUser?.uid?:""

        if(userId != null){
            val userReference = databaseReference.getReference().child("user").child(userId)
            val userData = hashMapOf(
                "userName" to name,
                "address" to address,
                "email" to email,
                "phone" to phone
            )
            userReference.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(), "Information Updated Successfully!", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Information Updation Failed!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setUserData() {
        val userId = auth.currentUser?.uid
        if(userId != null){
            val userReference = databaseReference.getReference().child("user").child(userId)

            userReference.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            val userProfile = snapshot.getValue(UserModel::class.java)
                            if(userProfile != null){
                                binding.Name.setText(userProfile.userName)
                                binding.Address.setText(userProfile.address)
                                binding.Email.setText(userProfile.email)
                                binding.Phone.setText(userProfile.phone)
                            }
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }
    }
}