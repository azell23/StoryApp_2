package com.example.mystoryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.mystoryapp.repository.MainRepo
import com.example.mystoryapp.response.ListStoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainModel@Inject constructor(private val mainRepo: MainRepo) : ViewModel(){

    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = _loading


    fun getAllStory(): LiveData<PagingData<ListStoryItem>> = mainRepo.getAllStory()

}