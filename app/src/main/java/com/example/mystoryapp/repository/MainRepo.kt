package com.example.mystoryapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.mystoryapp.api.Service
import com.example.mystoryapp.datastore.AuthPreference
import com.example.mystoryapp.response.ListStoryItem
import javax.inject.Inject

class MainRepo@Inject constructor(private val story: com.example.mystoryapp.database.Story, private val apiService: Service, private val preference: AuthPreference) {

    fun getAllStory(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 5),
            remoteMediator = com.example.mystoryapp.remotemediator.Story(story, apiService, preference), pagingSourceFactory = {
                story.storyDao().getAllStory()
            }
        ).liveData
    }
}