package com.example.gluttony

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gluttony.adaptar.menuAdaptar
import com.example.gluttony.databinding.FragmentMenuBottomSheetBinding
import com.example.gluttony.models.MenuItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.FirebaseDatabase.getInstance
import com.google.firebase.database.ValueEventListener


class menuBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding:FragmentMenuBottomSheetBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBottomSheetBinding.inflate(inflater,container,false)
        binding.MenuBottomSheetBackArrow.setOnClickListener(){
            dismiss()
        }
        retrieveMenuItems()
        return binding.root
    }
    private fun retrieveMenuItems() {
        database = FirebaseDatabase.getInstance()
        val foodRef : DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()
        foodRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               for(foodSnapshot in snapshot.children ){
                   val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                   menuItem?.let { menuItems.add(it) }
               }
                setAdapter()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    private fun setAdapter() {
        val adapter = menuAdaptar(menuItems, requireContext())
        binding.MenuBottomSheetRV.layoutManager = LinearLayoutManager(requireContext())
        binding.MenuBottomSheetRV.adapter = adapter
    }
    companion object {
    }
}