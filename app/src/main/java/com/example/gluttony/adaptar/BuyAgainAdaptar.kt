package com.example.gluttony.adaptar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gluttony.databinding.BuyAgainItemBinding

class BuyAgainAdaptar(private val buyAgainFoodName: Array<String>,
                      private val buyAgainFoodPrice: Array<String>,
                      private val buyAgainFoodImage: Array<Int>
):RecyclerView.Adapter<BuyAgainAdaptar.BuyAgainViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyAgainViewHolder {
        val binding = BuyAgainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BuyAgainViewHolder(binding)
    }
    override fun onBindViewHolder(holder: BuyAgainViewHolder, position: Int) {
       holder.bind(buyAgainFoodName[position], buyAgainFoodPrice[position], buyAgainFoodImage[position])
    }
    override fun getItemCount(): Int = buyAgainFoodImage.size

    class BuyAgainViewHolder(private val binding: BuyAgainItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(foodName: String, foodPrice: String, foodImage: Int) {
                binding.BuyAgainFoodName.text = foodName
                binding.BuyAgainFoodPrice.text = foodPrice
                binding.BuyAgainFoodImage.setImageResource(foodImage)
        }

    }
}