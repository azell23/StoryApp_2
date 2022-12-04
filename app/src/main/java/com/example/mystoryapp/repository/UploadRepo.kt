package com.example.mystoryapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mystoryapp.api.Config
import com.example.mystoryapp.response.UploadStory
import com.example.mystoryapp.viewmodel.UploadModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadRepo  {

    private val storyResponse = MutableLiveData<UploadStory>()
    private val _loading = MutableLiveData<Boolean>()

    fun uploadStory(token: String, image: MultipartBody.Part, desc: RequestBody): LiveData<UploadStory> {
        _loading.postValue(true)
        Config.getApiService().uploadImage(token, image, desc)
            .enqueue(object : Callback<UploadStory> {
                override fun onResponse(call: Call<UploadStory>, response: Response<UploadStory>) {
                    _loading.postValue(false)
                    if (response.isSuccessful){
                        storyResponse.postValue(response.body())
                        Log.d("yuk bisa","${response.body()}")
                    }
                }

                override fun onFailure(call: Call<UploadStory>, t: Throwable) {
                    Log.e(UploadModel::class.java.simpleName, "onFailure Call ${t.message}")
                }
            })
        return storyResponse
    }

    fun uploadStoryWithLocation(token: String, image: MultipartBody.Part, desc: RequestBody, lat:Double?, lon: Double?): LiveData<UploadStory>{
        _loading.postValue(true)
        Config.getApiService().uploadImageWithLocations(token,image,desc,lat,lon)
            .enqueue(object : Callback<UploadStory> {
                override fun onResponse(call: Call<UploadStory>, response: Response<UploadStory>) {
                    _loading.postValue(false)
                    if (response.isSuccessful){
                        storyResponse.postValue(response.body())
                        Log.d("upload with location","${response.message()}")
                    }
                }

                override fun onFailure(call: Call<UploadStory>, t: Throwable) {
                    Log.e(UploadModel::class.java.simpleName, "onFailure Call ${t.message}")
                }

            })
        return storyResponse
    }
}