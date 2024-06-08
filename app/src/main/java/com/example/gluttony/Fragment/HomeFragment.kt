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
import com.example.gluttony.databinding.FragmentHomeBinding
import com.example.gluttony.menuBottomSheetFragment


class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding

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
        return binding.root
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
                TODO("Not Yet Implemented")
            }

            override fun onItemSelected(position: Int) {
               // val itemPostion = imageList[position]
                val itemMassage = "Selected Item $position"
                Toast.makeText(requireContext(), itemMassage,Toast.LENGTH_SHORT).show()

            }
        })

        val foodName = listOf("Buger","Momos","Fries","Pizza")
        val foodPrice = listOf("$5","$7","$3","$15")
        val foodImage = listOf(R.drawable.menu1,R.drawable.menu2,R.drawable.menu3,R.drawable.menu4)

        val adaptar = PopularAdaptar(foodName,foodImage,foodPrice, requireContext())
        binding.popularRv.layoutManager=LinearLayoutManager(requireContext())
        binding.popularRv.adapter = adaptar
    }
    companion object {

    }
}