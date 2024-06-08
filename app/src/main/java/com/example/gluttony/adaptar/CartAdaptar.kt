package com.example.gluttony.adaptar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gluttony.databinding.CartItemBinding

class CartAdaptar(private val cartItems:MutableList<String>, private val cartItemPrice: MutableList<String>, private val cartImages:MutableList<Int>):RecyclerView.Adapter<CartAdaptar.CartViewHolder>() {

    private val itemQuantities = IntArray(cartItems.size){1}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }

   inner class CartViewHolder(private val binding: CartItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                cartFoodName.text = cartItems[position]
                cartFoodPrice.text = cartItemPrice[position]
                cartFoodImage.setImageResource(cartImages[position])
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
               binding.cartItemQuantity.text=itemQuantities[position].toString()

           }
       }
       private fun decreaseQuantity(position:Int){
           if(itemQuantities[position]>1){
               itemQuantities[position]--
               binding.cartItemQuantity.text=itemQuantities[position].toString()
           }
       }
       private fun deleteItemFromCart(position: Int){
           cartItems.removeAt(position)
           cartImages.removeAt(position)
           cartItemPrice.removeAt(position)
           notifyItemRemoved(position)
           notifyItemRangeChanged(position,cartItems.size)
       }


    }

}