package com.example.mystoryapp.repository

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mystoryapp.api.Config
import com.example.mystoryapp.response.ListStoryItem
import com.example.mystoryapp.response.Story
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsRepo {

    private val _mapStory = MutableLiveData<List<ListStoryItem>>()

    fun getStoryWithMap(token: String): LiveData<List<ListStoryItem>> {
        Config.getApiService().getStoriesWithMap(token, 1)
            .enqueue(object : Callback<Story> {
                override fun onResponse(call: Call<Story>, response: Response<Story>) {
                    if (response.isSuccessful){
                        _mapStory.postValue(response.body()?.listStory)
                    }
                }
                override fun onFailure(call: Call<Story>, t: Throwable) {
                    Log.e(ContentValues.TAG,"Error viewmodel, ${t.message.toString()}")
                }
            })
        return _mapStory
    }
    fun getStory(): LiveData<List<ListStoryItem>>{
        return _mapStory
    }
}