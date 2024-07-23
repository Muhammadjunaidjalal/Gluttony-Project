package com.example.gluttony.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gluttony.R
import com.example.gluttony.adaptar.menuAdaptar
import com.example.gluttony.databinding.FragmentSearchBinding
import android.widget.SearchView

class SearchFragment : Fragment() {
    private lateinit var binding:FragmentSearchBinding
    private lateinit var adaptar : menuAdaptar
    private val originalMenuFoodName = listOf("Burger", "Memo", "Sandwich", "Pizza", "Fries","Memo", "Sandwich", "Pizza", "Fries")
    private val originalMenuFoodPrice = listOf("$5","$3", "$7", "$15","$3","$3", "$7", "$15","$3")
    private val originalMenuFoodImages = listOf(R.drawable.menu1,
        R.drawable.menu2, R.drawable.menu3,R.drawable.menu2,R.drawable.menu4,
        R.drawable.menu2, R.drawable.menu3,R.drawable.menu2,R.drawable.menu4)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private val filteredMenuFoodName = mutableListOf<String>()
    private val filteredMenuFoodPrice = mutableListOf<String>()
    private val filteredMenuImages = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
/*binding = FragmentSearchBinding.inflate(inflater,container,false)
        adaptar = menuAdaptar(filteredMenuFoodName,filteredMenuFoodPrice,filteredMenuImages, requireContext())
        binding.SearchRV.layoutManager= LinearLayoutManager(requireContext())
        binding.SearchRV.adapter=adaptar*/

        setupSearchView()
        showAllMenu()
        return binding.root
    }
private fun showAllMenu(){

    filteredMenuFoodName.clear()
    filteredMenuFoodPrice.clear()
    filteredMenuImages.clear()

    filteredMenuFoodName.addAll(originalMenuFoodName)
    filteredMenuFoodPrice.addAll(originalMenuFoodPrice)
    filteredMenuImages.addAll(originalMenuFoodImages)

    adaptar.notifyDataSetChanged()

}
    private fun setupSearchView(){
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
               filterMenuItems(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterMenuItems(newText)
                return true
            }
        })
    }

    private fun filterMenuItems(query:String){
        filteredMenuFoodName.clear()
        filteredMenuFoodPrice.clear()
        filteredMenuImages.clear()

        originalMenuFoodName.forEachIndexed{index, foodName->
            if(foodName.contains(query, ignoreCase = true)){

                filteredMenuFoodName.add(foodName)
                filteredMenuFoodPrice.add(originalMenuFoodPrice[index])
                filteredMenuImages.add(originalMenuFoodImages[index])

            }
        }
        adaptar.notifyDataSetChanged()
    }
    companion object {

    }
}