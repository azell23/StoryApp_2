package com.example.mystoryapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mystoryapp.databinding.ActivityDetailBinding
import com.example.mystoryapp.response.ListStoryItem
import com.example.mystoryapp.util.Constanta
import com.example.mystoryapp.util.Constanta.Companion.setLocalDate

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        detailData()
    }
    private fun detailData(){
        val image = binding.ivDetailPost
        val name = binding.tvDetailName
        val desc = binding.tvDetailDesc
        val date = binding.tvDetailDate
        val lat = binding.tvLat
        val lon = binding.tvLon
        val detail = intent.getParcelableExtra<ListStoryItem>(Constanta.DETAIL) as ListStoryItem
        Glide.with(this)
            .load(detail.photoUrl)
            .into(image)
        name.text = detail.name
        desc.text = detail.description
        date.setLocalDate(detail.createdAt)
        lat.text = detail.lat.toString()
        lon.text = detail.lon.toString()


    }

    companion object{


        const val DETAIL_MARK = "detail_mark"
    }
}