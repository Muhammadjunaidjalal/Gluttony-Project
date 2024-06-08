package com.example.gluttony

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gluttony.adaptar.NotificationAdaptar
import com.example.gluttony.databinding.FragmentNotificationBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class Notification_Bottom_Sheet_Fragment : BottomSheetDialogFragment() {
   lateinit var binding:FragmentNotificationBottomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationBottomBinding.inflate(layoutInflater,container,false)
        binding.NotificationBottomSheetBackArrow.setOnClickListener(){
            dismiss()
        }
        setUpAdapter()
        return binding.root
    }
        private fun setUpAdapter(){
            val notificationText = arrayOf("Your Order Has Been Canceled!", "Your Order Has Been Shipped!", "Your Order Has Been Placed!")
            val notificationImages = arrayOf(R.drawable.sademoji, R.drawable.truck, R.drawable.confirm_order)
            val adaptar = NotificationAdaptar(notificationText, notificationImages)
            binding.NotificationRv.layoutManager = LinearLayoutManager(requireContext())
            binding.NotificationRv.adapter = adaptar

        }
    companion object {

    }
}