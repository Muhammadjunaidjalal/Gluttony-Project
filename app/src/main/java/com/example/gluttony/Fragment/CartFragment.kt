package com.example.gluttony.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gluttony.PayOutActivity
import com.example.gluttony.R
import com.example.gluttony.adaptar.CartAdaptar
import com.example.gluttony.databinding.FragmentCartBinding
import com.example.gluttony.models.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartFragment : Fragment() {

    private lateinit var binding :FragmentCartBinding
    private lateinit var auth :FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userId:String
    private lateinit var foodNames:MutableList<String>
    private lateinit var foodPrices:MutableList<String>
    private lateinit var foodImagesUri:MutableList<String>
    private lateinit var foodDescriptions:MutableList<String>
    private lateinit var foodIngredients:MutableList<String>
    private lateinit var quantity:MutableList<Int>
    private lateinit var cartAdapter:CartAdaptar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(layoutInflater, container,false)
        auth = FirebaseAuth.getInstance()
        retrieveCartItems()

        binding.ProceedButton.setOnClickListener(){
            getOrderedItemDetail()
        }
        return binding.root
    }

    private fun getOrderedItemDetail() {
        val orderIdReference = database.reference.child("user").child(userId).child("CartItems")

        val foodName = mutableListOf<String>()
        val foodPrice = mutableListOf<String>()
        val foodImage = mutableListOf<String>()
        val foodDescription = mutableListOf<String>()
        val foodIngredients = mutableListOf<String>()
        val foodQuantity = cartAdapter.getUpdatedFoodQuantities()

        orderIdReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(foodSnapshot in snapshot.children){
                    val orderItems = foodSnapshot.getValue(CartItems::class.java)
                    orderItems?.foodName?.let { foodName.add(it) }
                    orderItems?.foodPrice?.let { foodPrice.add(it) }
                    orderItems?.foodDescription?.let { foodDescription.add(it) }
                    orderItems?.foodImage?.let { foodImage.add(it) }
                    orderItems?.foodIngredients.let {
                        if (it != null) {
                            foodIngredients.add(it)
                        }
                    }

                    orderNow(foodName, foodPrice, foodDescription, foodImage, foodIngredients, foodQuantity)

                }
            }

            private fun orderNow(
                foodName: MutableList<String>,
                foodPrice: MutableList<String>,
                foodDescription: MutableList<String>,
                foodImage: MutableList<String>,
                foodIngredients: MutableList<String>,
                foodQuantity: MutableList<Int>
            ) {
                if(isAdded && context!= null) {
                    val intent = Intent(requireContext(), PayOutActivity::class.java)
                    intent.putExtra("FoodItemName", foodName as ArrayList<String>)
                    intent.putExtra("FoodItemPrice", foodPrice as ArrayList<String>)
                    intent.putExtra("FoodItemImage", foodImage as ArrayList<String>)
                    intent.putExtra("FoodItemDescription", foodDescription as ArrayList<String>)
                    intent.putExtra("FoodItemIngredients" ,foodIngredients as ArrayList<String>)
                    intent.putExtra("FoodItemQuantities", foodQuantity as ArrayList<Int>)
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun retrieveCartItems() {
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid?:""
        val foodReference = database.reference.child("user").child(userId).child("CartItems")

        foodNames = mutableListOf()
        foodPrices = mutableListOf()
        foodIngredients = mutableListOf()
        foodDescriptions = mutableListOf()
        foodImagesUri = mutableListOf()
        quantity = mutableListOf()

        foodReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                    for(foodSnapshot in snapshot.children){
                        val cartItems = foodSnapshot.getValue(CartItems::class.java)

                        cartItems?.foodName?.let { foodNames.add(it) }
                        cartItems?.foodPrice?.let { foodPrices.add(it) }
                        cartItems?.foodDescription?.let { foodDescriptions.add(it) }
                        cartItems?.foodIngredients?.let { foodIngredients.add(it) }
                        cartItems?.foodQuantity?.let { quantity.add(it) }
                        cartItems?.foodImage?.let { foodImagesUri.add(it) }

                    }
                setAdapter()
            }

            private fun setAdapter() {
                cartAdapter = CartAdaptar(requireContext(),foodNames,foodPrices, foodImagesUri, foodDescriptions, quantity, foodIngredients)
                binding.cartRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.cartRv.adapter = cartAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}