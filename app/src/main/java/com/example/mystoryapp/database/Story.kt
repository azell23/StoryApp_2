package com.example.mystoryapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mystoryapp.response.ListStoryItem


@Database(entities = [ListStoryItem::class, RemoteKeys::class] ,
version = 1,
exportSchema = false)

abstract class Story: RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object{
        @Volatile
        private var INSTANCE: Story? = null

        @JvmStatic
        fun getInstance (context: Context): Story{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    Story::class.java, "database_story"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}