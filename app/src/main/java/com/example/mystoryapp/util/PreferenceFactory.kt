package com.example.mystoryapp.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.datastore.AuthPreference
import com.example.mystoryapp.repository.AuthRepo
import com.example.mystoryapp.viewmodel.*

class PreferenceFactory(private val authPreference: AuthPreference, private val authRepo: AuthRepo, private val context: Context): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Auth::class.java)){
            return Auth(authRepo) as T
        }
        if (modelClass.isAssignableFrom(MainModel::class.java)){
            return MainModel(Injection.storyRepo(authPreference, context)) as T
        }
        if (modelClass.isAssignableFrom(StoryMaps::class.java)){
            return StoryMaps(Injection.mapsRepo()) as T
        }
        if (modelClass.isAssignableFrom(Paging::class.java)){
            return Paging(Injection.storyRepo(authPreference, context)) as T
        }
        if (modelClass.isAssignableFrom(UploadModel::class.java)){
            return UploadModel(Injection.uploadRepo()) as T
        }
        throw IllegalArgumentException("error ${modelClass.name}")
    }


}