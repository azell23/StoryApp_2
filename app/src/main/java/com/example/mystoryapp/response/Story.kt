package com.example.mystoryapp.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class Story(

	@field:SerializedName("listStory")
	val listStory: List<ListStoryItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
@Entity(tableName = "story")
@Parcelize
data class ListStoryItem(

	@field:SerializedName("photoUrl")
	val photoUrl: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@PrimaryKey
	@field:SerializedName("id")
	val id: String,

	val lat: Double? = null,
	val lon: Double? = null
): Parcelable

//@Entity(tableName = "story")
//@Parcelize
//data class ListStory(
//	@PrimaryKey
//	val id: String,
//	val name: String,
//	val description: String,
//	val photoUrl: String,
//	val createdAt: String,
//	val lon: Double,
//	val lat: Double
//): Parcelable