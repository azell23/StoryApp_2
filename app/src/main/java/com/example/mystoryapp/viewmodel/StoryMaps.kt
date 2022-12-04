package com.example.mystoryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapp.repository.MapsRepo
import com.example.mystoryapp.response.ListStoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StoryMaps@Inject constructor(private val mapsRepo: MapsRepo): ViewModel() {


    fun getStoryWithMaps(token: String): LiveData<List<ListStoryItem>> =
        mapsRepo.getStoryWithMap(token)

    fun getStory():LiveData<List<ListStoryItem>> = mapsRepo.getStory()

}