package com.example.gluttony

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gluttony.databinding.ActivityPayOutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PayOutActivity : AppCompatActivity() {
    lateinit var binding: ActivityPayOutBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var name:String
    private lateinit var address:String
    private lateinit var phone:String
    private lateinit var totalAmount:String
    private lateinit var foodItemName:ArrayList<String>
    private lateinit var foodItemPrice:ArrayList<String>
    private lateinit var foodItemImage:ArrayList<String>
    private lateinit var foodItemDescription:ArrayList<String>
    private lateinit var foodItemIngredients:ArrayList<String>
    private lateinit var foodItemQuantities:ArrayList<Int>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth= FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference()

        setUserData()

        val intent = intent
        foodItemName = intent.getStringArrayListExtra("FoodItemName") as ArrayList<String>
        foodItemPrice = intent.getStringArrayListExtra("FoodItemPrice") as ArrayList<String>
        foodItemImage = intent.getStringArrayListExtra("FoodItemImage") as ArrayList<String>
        foodItemDescription = intent.getStringArrayListExtra("FoodItemDescription") as ArrayList<String>
        foodItemIngredients = intent.getStringArrayListExtra("FoodItemIngredients") as ArrayList<String>
        foodItemQuantities = intent.getIntegerArrayListExtra("FoodItemQuantities") as ArrayList<Int>

        totalAmount = calculateTotalAmount().toString() +"$"
        binding.TotalAmount.isEnabled = false
        binding.TotalAmount.setText(totalAmount)
        binding.PayoutBackArrow.setOnClickListener{
            finish()
        }
        binding.PlaceOrderButton.setOnClickListener{
            name = binding.Name.text.toString().trim()
            phone = binding.Phone.text.toString().trim()
            address = binding.Address.text.toString().trim()
            if(name.isBlank() && phone.isBlank() && address.isBlank()){
                Toast.makeText(this, "Fill All The Details!", Toast.LENGTH_SHORT).show()
            }else{
                placeOrder()
            }
            val bottomSheetDialog = CongratsBottomSheetFragment()
            bottomSheetDialog.show(supportFragmentManager,"Test")
        }
    }

    private fun placeOrder() {

    }

    private fun calculateTotalAmount(): Int {
        var totalAmount = 0
        for (i in 0 until foodItemPrice.size){
            var price = foodItemPrice[i]
            val lastChar = price.last()
            val priceIntVal = if(lastChar == '$'){
                price.dropLast(1).toInt()
            }else{
                price.toInt()
            }
            var quantity = foodItemQuantities[i]
            totalAmount+= priceIntVal * quantity
        }
        return totalAmount
    }
   /*private fun calculateTotalAmount(): Int {
       var totalAmount = 0
       for (i in 0 until foodItemPrice.size) {
           var price = foodItemPrice[i]
           val lastChar = price.last()
           val priceIntVal = when {
               lastChar == '$' -> price.dropLast(1).toInt()
               price.startsWith("Rs") -> price.removePrefix("Rs").toInt()
               else -> price.toInt()
           }
           val quantity = foodItemQuantities[i]
           totalAmount += priceIntVal * quantity
       }
       return totalAmount
   }*/


    private fun setUserData() {
        val user = auth.currentUser
        if(user!=null){
            val userId = user.uid
            val userReference = databaseReference.child("user").child(userId)

            userReference.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            val names  = snapshot.child("userName").getValue(String::class.java)
                            val addresses = snapshot.child("address").getValue(String::class.java)
                            val phones = snapshot.child("phone").getValue(String::class.java)
                            binding.apply {
                                Name.setText(names)
                                Address.setText(addresses)
                                Phone.setText(phones)
                            }
                        }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }
}