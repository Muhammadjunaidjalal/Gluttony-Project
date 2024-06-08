package com.example.gluttony.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gluttony.R
import com.example.gluttony.adaptar.BuyAgainAdaptar
import com.example.gluttony.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
   private lateinit var binding: FragmentHistoryBinding
   private lateinit var buyAgainAdapter : BuyAgainAdaptar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater,container,false)
        setUpRecyclerView()
        return binding.root
    }
private fun setUpRecyclerView(){
    val buyAgainName = arrayOf("Food 1", "Food 2", "Food 3")
    val buyAgainPrice = arrayOf("$3","$5","$7")
    val buyAgainImages = arrayOf(R.drawable.menu1, R.drawable.menu2, R.drawable.menu3)
    buyAgainAdapter = BuyAgainAdaptar(buyAgainName, buyAgainPrice, buyAgainImages)
    binding.BuyAgainRV.adapter = buyAgainAdapter
    binding.BuyAgainRV.layoutManager = LinearLayoutManager(requireContext())
}
    companion object {

    }
}