package com.example.gluttony.adaptar

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gluttony.DetailsActivity
import com.example.gluttony.databinding.MenuItemBinding

class menuAdaptar(private val menuItems: List<String>, private val menuFoodprice: List<String>,
                  private val menuFoodImages: List<Int>, private val requireContext: Context
):RecyclerView.Adapter<menuAdaptar.MenuViewHolder>() {
    private val itemClickListener : OnClickListener ?= null
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
                   itemClickListener?.onItemClick(position)
               }

               val intent = Intent(requireContext, DetailsActivity::class.java)
               intent.putExtra("MenuFoodName", menuItems.get(position))
               intent.putExtra("MenuFoodImage",menuFoodImages.get(position))
               requireContext.startActivity(intent)

           }
       }

        fun bind(position: Int) {
            binding.apply{
                MenuFoodName.text= menuItems[position]
                MenuFoodPrice.text=menuFoodprice[position]
                MenuFoodImage.setImageResource(menuFoodImages[position])
            }
        }

    }

    interface OnClickListener {

        fun onItemClick(position: Int) {
        }
    }

}

