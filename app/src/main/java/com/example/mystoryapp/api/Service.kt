package com.example.mystoryapp.api

import com.example.mystoryapp.response.Login
import com.example.mystoryapp.response.Register
import com.example.mystoryapp.response.Story
import com.example.mystoryapp.response.UploadStory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Service {

    @GET("stories")
    fun getStoryList(
        @Header("Authorization") token: String,
        @Query("size") size: Int
    ): Call<Story>

    @GET("stories")
    suspend fun getAllStory(
        @Header("Authorization") token: String,
        @Query("size") size: Int,
        @Query("page") page: Int,
    ) :Story

    @GET("stories")
    fun getStoriesWithMap(
        @Header("Authorization") token: String,
        @Query("location") location : Int
    ): Call<Story>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part file :MultipartBody.Part,
        @Part ("description") description : RequestBody,
    ): Call<UploadStory>

    @Multipart
    @POST("stories")
    fun uploadImageWithLocations(
        @Header("Authorization") token: String,
        @Part file :MultipartBody.Part,
        @Part ("description") description : RequestBody,
        @Part ("lat") lat: Double?,
        @Part ("lon") lon: Double?
    ): Call<UploadStory>

    @POST("register")
    @FormUrlEncoded
    fun register(
        @Field("name") name :String,
        @Field("email") email :String,
        @Field("password") password :String,
    ): Call<Register>
    @POST("login")
    @FormUrlEncoded
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<Login>

}