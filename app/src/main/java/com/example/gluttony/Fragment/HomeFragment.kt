package com.example.gluttony.Fragment

import android.os.Bundle
import android.os.TestLooperManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.gluttony.R
import com.example.gluttony.adaptar.PopularAdaptar
import com.example.gluttony.adaptar.menuAdaptar
import com.example.gluttony.databinding.FragmentHomeBinding
import com.example.gluttony.menuBottomSheetFragment
import com.example.gluttony.models.MenuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.ViewMenuBtn.setOnClickListener(){
            val bottomSheetDialog = menuBottomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager,"Test")
        }
        retrieveAndDisplayPopularItem()
        return binding.root
    }
    //Retrieving data from the firebase database
    private fun retrieveAndDisplayPopularItem() {
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")
        menuItems  = mutableListOf()

        foodRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(foodSnapshot in snapshot.children){
                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                randomPopularItems()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    //Function for shulling the data
    private fun randomPopularItems() {
        val index = menuItems.indices.toList().shuffled()
        val numItemToShow = 6
        val subsetMenuItems = index.take(numItemToShow).map { menuItems[it] }

        setPopularItemsAdapter(subsetMenuItems)
    }
    //Function for setting up data into the adapter
    private fun setPopularItemsAdapter(subsetMenuItems: List<MenuItem>) {
        val adaptar = menuAdaptar(subsetMenuItems, requireContext())
        binding.popularRv.layoutManager=LinearLayoutManager(requireContext())
        binding.popularRv.adapter = adaptar
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner4, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner3, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner4, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner5, ScaleTypes.FIT))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList,ScaleTypes.FIT)
        imageSlider.setItemClickListener(object: ItemClickListener{
            override fun doubleClick(position: Int){
            }
            override fun onItemSelected(position: Int) {
               // val itemPostion = imageList[position]
                val itemMassage = "Selected Item $position"
                Toast.makeText(requireContext(), itemMassage,Toast.LENGTH_SHORT).show()
            }
        })
    }
}