package com.example.mystoryapp.data

import com.example.mystoryapp.api.Service
import com.example.mystoryapp.response.Login
import com.example.mystoryapp.response.Register
import com.example.mystoryapp.response.Story
import com.example.mystoryapp.response.UploadStory
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class FakeApi: Service {

    private val dummyList = Dummy.DummyListStory()
    override fun getStoryList(token: String, size: Int): Call<Story> {
        TODO("Not yet implemneyed")
    }

    override suspend fun getAllStory(token: String, size: Int, page: Int): Story {
        return dummyList
    }

    override fun getStoriesWithMap(token: String, location: Int): Call<Story> {
        TODO("Not yet implemented")
    }

    override fun uploadImage(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): Call<UploadStory> {
        TODO("Not yet implemented")
    }

    override fun uploadImageWithLocations(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: Double?,
        lon: Double?
    ): Call<UploadStory> {
        TODO("Not yet implemented")
    }

    override fun register(name: String, email: String, password: String): Call<Register> {
        TODO("Not yet implemented")
    }

    override fun login(email: String, password: String): Call<Login> {
        TODO("not yet implemented")
    }


}