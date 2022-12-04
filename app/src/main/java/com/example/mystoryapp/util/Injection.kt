package com.example.mystoryapp.util

import android.content.Context
import com.example.mystoryapp.api.Config
import com.example.mystoryapp.database.Story
import com.example.mystoryapp.datastore.AuthPreference
import com.example.mystoryapp.repository.MainRepo
import com.example.mystoryapp.repository.MapsRepo
import com.example.mystoryapp.repository.UploadRepo

object Injection {

    fun storyRepo(authPreference: AuthPreference, context: Context): MainRepo{
        val servie = Config.getApiService()
        val database = Story.getInstance(context)
        return MainRepo(database,servie,authPreference)
    }
    fun mapsRepo(): MapsRepo{
        return MapsRepo()
    }
    fun uploadRepo(): UploadRepo{
        return UploadRepo()
    }

}