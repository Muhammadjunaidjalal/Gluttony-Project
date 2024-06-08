package com.example.gluttony.adaptar

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gluttony.DetailsActivity
import com.example.gluttony.databinding.PopularItemsBinding

class PopularAdaptar(private val items:List<String>, private val images:List<Int>, private val price:List<String>, private val requireContext:Context) :RecyclerView.Adapter<PopularAdaptar.PopularViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        return PopularViewHolder(PopularItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        val item = items[position]
        val images = images[position]
        val price = price[position]
        holder.bind(item,images,price)

        holder.itemView.setOnClickListener(){

            val intent = Intent(requireContext, DetailsActivity::class.java)
            intent.putExtra("MenuFoodName", item)
            intent.putExtra("MenuFoodImage", images)
            requireContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class PopularViewHolder(private val binding: PopularItemsBinding) :RecyclerView.ViewHolder(binding.root) {
        private val imageView = binding.popularImg
        fun bind(item: String, images: Int, price: String) {
            binding.foodNamePopular.text = item
            binding.pricePopular.text = price
            imageView.setImageResource(images)
        }

    }
}