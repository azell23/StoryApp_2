package com.example.mystoryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystoryapp.repository.MainRepo
import com.example.mystoryapp.response.ListStoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class Paging @Inject constructor(repository: MainRepo): ViewModel() {
    val getStory: LiveData<PagingData<ListStoryItem>> = repository.getAllStory().cachedIn(viewModelScope)
}