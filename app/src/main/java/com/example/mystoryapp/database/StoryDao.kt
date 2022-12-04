package com.example.mystoryapp.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mystoryapp.response.ListStoryItem


@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStory(listStoryItem: ListStoryItem)

    @Query("Select * from story ORDER BY createdAt DESC")
    fun getAllStory():PagingSource<Int, ListStoryItem>

    @Query("Delete from story")
    suspend fun deleteStory()
}