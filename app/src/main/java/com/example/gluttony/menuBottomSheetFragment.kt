package com.example.gluttony

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gluttony.adaptar.menuAdaptar
import com.example.gluttony.databinding.FragmentMenuBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class menuBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding:FragmentMenuBottomSheetBinding

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

        val menuFoodName = listOf("Burger", "Memo", "Sandwich", "Pizza", "Fries","Memo", "Sandwich", "Pizza", "Fries")
        val menuFoodPrice = listOf("$5","$3", "$7", "$15","$3","$3", "$7", "$15","$3")
        val menuFoodImage = listOf(R.drawable.menu1,
            R.drawable.menu2, R.drawable.menu3,R.drawable.menu2,R.drawable.menu4,
            R.drawable.menu2, R.drawable.menu3,R.drawable.menu2,R.drawable.menu4)

        val adaptar = menuAdaptar(ArrayList(menuFoodName), ArrayList(menuFoodPrice),ArrayList(menuFoodImage), requireContext())
        binding.MenuBottomSheetRV.layoutManager = LinearLayoutManager(requireContext())
        binding.MenuBottomSheetRV.adapter = adaptar

        return binding.root
    }

    companion object {
    }
}