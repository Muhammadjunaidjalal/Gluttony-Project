package com.example.gluttony.adaptar

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gluttony.DetailsActivity
import com.example.gluttony.databinding.MenuItemBinding
import com.example.gluttony.models.MenuItem

class menuAdaptar(private val menuItems:List<MenuItem>, private val requireContext: Context
):RecyclerView.Adapter<menuAdaptar.MenuViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
       val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MenuViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }
    override fun getItemCount(): Int {
        return menuItems.size
    }
    inner class MenuViewHolder(private val binding: MenuItemBinding):RecyclerView.ViewHolder(binding.root) {
       init {
           binding.root.setOnClickListener(){
               val position = adapterPosition
               if (position != RecyclerView.NO_POSITION){
                   openDetailsActivity(position)
               }
           }
       }
        private fun openDetailsActivity(position: Int) {
            val menuItem = menuItems[position]

            val intent = Intent(requireContext, DetailsActivity::class.java).apply {
                putExtra("MenuItemName", menuItem.foodName)
                putExtra("MenuItemImageUrl",menuItem.foodImage)
                putExtra("MenuItemPrice", menuItem.foodPrice)
                putExtra("MenuItemIngredients", menuItem.foodIngredients)
                putExtra("MenuItemDescription", menuItem.foodDescription)
            }
            requireContext.startActivity(intent)
        }
        fun bind(position: Int) {
            val menuItem = menuItems[position]
            binding.apply{
                MenuFoodName.text= menuItem.foodName
                MenuFoodPrice.text=menuItem.foodPrice
                val uri = Uri.parse(menuItem.foodImage)
                Glide.with(requireContext).load(uri).into(MenuFoodImage)
            }
        }
    }
}

