package com.example.gluttony.adaptar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gluttony.databinding.NotificationItemBinding

class NotificationAdaptar(private val notification: Array<String>, private val notificationImage: Array<Int>):RecyclerView.Adapter<NotificationAdaptar.NotificationViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = NotificationItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotificationViewHolder(binding)
    }
    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(position)
    }
    override fun getItemCount(): Int = notification.size
    inner class NotificationViewHolder(private val binding:NotificationItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                NotificationTextView.text = notification[position]
                NotificationImageView.setImageResource(notificationImage[position])
            }

        }

    }
}