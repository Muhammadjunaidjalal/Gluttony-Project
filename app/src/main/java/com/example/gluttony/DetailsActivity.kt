package com.example.gluttony

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.gluttony.databinding.ActivityDetailsBinding
import com.example.gluttony.models.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private  var foodName: String?=null
    private var foodPrice:String?=null
    private  var foodDescription: String?= null
    private var foodIngredients:String?=null
    private var foodImage:String?=null
    private lateinit var auth :FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()
        //Getting Data From Adapter
        foodName = intent.getStringExtra("MenuItemName")
        foodPrice = intent.getStringExtra("MenuItemPrice")
        foodDescription = intent.getStringExtra("MenuItemDescription")
        foodIngredients = intent.getStringExtra("MenuItemIngredients")
        foodImage = intent.getStringExtra("MenuItemImageUrl")

        // Setting Data to the Details Activity
        with(binding) {
            DetailsFoodNameTextView.text = foodName
            DetailsDescriptionTextView.text = foodDescription
            DetailsIngredientsTextView.text = foodIngredients
            Glide.with(this@DetailsActivity).load(Uri.parse(foodImage)).into(DetailsImageView)
        }
       binding.DetailsBackArrowButton.setOnClickListener() {
           finish()
       }
       binding.DetailsAddToCartButton.setOnClickListener(){
           addItemToCart()
       }
    }

    private fun addItemToCart() {
        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid?:""

        val cartItem = CartItems(foodName.toString(), foodPrice.toString(), foodDescription.toString(),foodImage.toString(),1)
        database.child("user").child(userId).child("CartItems").push().setValue(cartItem).addOnSuccessListener {
            Toast.makeText(this, "Item Added Into Cart!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener(){
            Toast.makeText(this,"Item Not Added!", Toast.LENGTH_SHORT).show()
        }
    }
}