package com.example.gluttony.adaptar

import android.app.PendingIntent.getActivity
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gluttony.databinding.CartItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartAdaptar(private val context: Context,
                  private val cartItems:MutableList<String>,
                  private val cartItemPrice: MutableList<String>,
                  private val cartImages:MutableList<String>,
                  private val cartDescription:MutableList<String>,
                  private val cartQuantity:MutableList<Int>,
                  private val cartIngredients: MutableList<String>
):RecyclerView.Adapter<CartAdaptar.CartViewHolder>() {
    private lateinit var auth: FirebaseAuth

    init {
        auth = FirebaseAuth.getInstance()
        val database= FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid?:""
        val cartItemNumber = cartItems.size

        itemQuantities = IntArray(cartItems.size){1}
        cartItemReference = database.reference.child("user").child(userId).child("CartItems")
    }
    companion object{
        private var itemQuantities : IntArray = intArrayOf()
        private lateinit var cartItemReference : DatabaseReference
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }
        // get updated quanitities
    fun getUpdatedFoodQuantities(): MutableList<Int> {
        val itemQuantity= mutableListOf<Int>()
        itemQuantity.addAll(cartQuantity)
        return itemQuantity
    }

    inner class CartViewHolder(private val binding: CartItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                cartFoodName.text = cartItems[position]
                cartFoodPrice.text = cartItemPrice[position]
                val uriString = cartImages[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(cartFoodImage)
                cartItemQuantity.text = quantity.toString()

                cartMinusBtn.setOnClickListener(){
                    decreaseQuantity(position)
                }
                cartPlusBtn.setOnClickListener(){
                    increaseQuantity(position)
                }
                cartDeleteBtn.setOnClickListener(){
                    val itemPosition = adapterPosition
                    if(itemPosition != RecyclerView.NO_POSITION){
                        deleteItemFromCart(itemPosition)
                    }
                }
            }
        }
       private fun increaseQuantity(position:Int){
           if(itemQuantities[position]<10){
               itemQuantities[position]++
               cartQuantity[position] = itemQuantities[position]
               binding.cartItemQuantity.text=itemQuantities[position].toString()

           }
       }
       private fun decreaseQuantity(position:Int){
           if(itemQuantities[position]>1){
               itemQuantities[position]--
               cartQuantity[position]= itemQuantities[position]
               binding.cartItemQuantity.text=itemQuantities[position].toString()
           }
       }
       private fun deleteItemFromCart(position: Int){
                val positionRetrieve = position
           getUniqueKeyAtPosition(positionRetrieve){uniqueKey->
               if(uniqueKey != null){
                   removeItem(position, uniqueKey)
               }
           }
       }

       private fun removeItem(position: Int, uniqueKey: String) {
           if(uniqueKey != null){
               cartItemReference.child(uniqueKey).removeValue().addOnSuccessListener {
                   cartItems.removeAt(position)
                   cartImages.removeAt(position)
                   cartDescription.removeAt(position)
                   cartQuantity.removeAt(position)
                   cartItemPrice.removeAt(position)
                   cartIngredients.removeAt(position)
                   Toast.makeText(context,"Item Removed!", Toast.LENGTH_SHORT).show()
                   itemQuantities = itemQuantities.filterIndexed { index, i -> index != position}.toIntArray()
                   notifyItemRemoved(position)
                   notifyItemRangeChanged(position, cartItems.size)
               }.addOnFailureListener{
                   Toast.makeText(context, "Failed to Delete!", Toast.LENGTH_SHORT).show()
               }
           }
       }

       private fun getUniqueKeyAtPosition(positionRetrieve: Int,  onComplete:(String?)->Unit) {
            cartItemReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var uniqueKey:String?=null
                    snapshot.children.forEachIndexed{index,dataSnapshot->
                        if(index == positionRetrieve){
                            uniqueKey = dataSnapshot.key
                            return@forEachIndexed
                        }
                    }
                    onComplete(uniqueKey)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
       }


   }

}