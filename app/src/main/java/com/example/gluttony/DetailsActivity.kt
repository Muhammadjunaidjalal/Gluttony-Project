package com.example.gluttony

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gluttony.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Getting Data From Adapter
        val foodName = intent.getStringExtra("MenuFoodName")
        val foodImage = intent.getIntExtra("MenuFoodImage", 0)

        // Setting Data to the Details Activity
        binding.DetailsFoodNameTextView.text = foodName
        binding.DetailsImageView.setImageResource(foodImage)
       binding.DetailsBackArrowButton.setOnClickListener(){
            finish()
        }
    }
}