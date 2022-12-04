package com.example.mystoryapp.response

import com.google.gson.annotations.SerializedName

data class UploadStory(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
