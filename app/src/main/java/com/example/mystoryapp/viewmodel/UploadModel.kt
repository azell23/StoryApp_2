package com.example.mystoryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapp.repository.UploadRepo
import com.example.mystoryapp.response.UploadStory
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class UploadModel@Inject constructor(private val uploadRepo: UploadRepo) :ViewModel() {

    fun upload(token: String, image: MultipartBody.Part, desc: RequestBody): LiveData<UploadStory> =
        uploadRepo.uploadStory(token, image, desc)

    fun uploadWithLocations(token: String, image: MultipartBody.Part, desc: RequestBody, lat:Double?, lon: Double?):LiveData<UploadStory> =
        uploadRepo.uploadStoryWithLocation(token, image, desc, lat, lon)

}