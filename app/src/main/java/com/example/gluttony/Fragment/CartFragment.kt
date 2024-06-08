package com.example.gluttony.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gluttony.PayOutActivity
import com.example.gluttony.R
import com.example.gluttony.adaptar.CartAdaptar
import com.example.gluttony.databinding.FragmentCartBinding

class CartFragment : Fragment() {

    lateinit var binding :FragmentCartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(layoutInflater, container,false)

        val cartFoodName = listOf("Burger", "Memo", "Sandwich", "Pizza", "Fries")
        val cartFoodPrice = listOf("$5","$3", "$7", "$15","$3")
        val cartFoodImage = listOf(R.drawable.menu1,
            R.drawable.menu2, R.drawable.menu3,R.drawable.menu2,R.drawable.menu4)

        val adaptar = CartAdaptar(ArrayList(cartFoodName), ArrayList(cartFoodPrice),ArrayList(cartFoodImage))
        binding.cartRv.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRv.adapter = adaptar
        binding.ProceedButton.setOnClickListener(){
            val intent = Intent(requireContext(),PayOutActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }
    companion object {
    }
}