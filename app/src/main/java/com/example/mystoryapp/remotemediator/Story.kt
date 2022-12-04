package com.example.mystoryapp.remotemediator

import androidx.paging.*
import androidx.room.withTransaction
import com.example.mystoryapp.api.Service
import com.example.mystoryapp.database.RemoteKeys
import com.example.mystoryapp.database.Story
import com.example.mystoryapp.datastore.AuthPreference
import com.example.mystoryapp.response.ListStoryItem
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class Story @Inject constructor(private val storyDatabase: Story, private val apiService: Service, private val preference: AuthPreference): RemoteMediator<Int, ListStoryItem>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListStoryItem>
    ): MediatorResult {

        val page = when(loadType){
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: initialPageIndex
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null) 
                nextKey
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeysForFirstItem(state)
                val prevKeys = remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKeys
            }
        }
        return try {
            val token: String = preference.getToken().first()
            val responseData = apiService.getAllStory("Bearer $token",state.config.pageSize, page)
            val endOfPaginationreache = responseData.listStory.isEmpty()
            
            storyDatabase.withTransaction { 
                if (loadType == LoadType.REFRESH){
                    storyDatabase.remoteKeysDao().deleteRemote()
                    storyDatabase.storyDao().deleteStory()
                }
                val prevKey = if (page == 1) null else page -1
                val nextKey = if (endOfPaginationreache) null else page +1
                val keys = responseData.listStory.map { 
                    RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                storyDatabase.remoteKeysDao().addAll(keys)
                responseData.listStory.forEach{ story ->
                    val item = ListStoryItem(
                        story.photoUrl,
                        story.createdAt,
                        story.name,
                        story.description,
                        story.id,
                        story.lat,
                        story.lon
                    )
                    storyDatabase.storyDao().addStory(item)
                }
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationreache)
        }catch (exception: Exception){
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeysForFirstItem(state: PagingState<Int, ListStoryItem>): RemoteKeys?{
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            storyDatabase.remoteKeysDao().getRemoteId(data.id)
        }
    }
    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ListStoryItem>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            storyDatabase.remoteKeysDao().getRemoteId(data.id)
        }
    }
    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ListStoryItem>): RemoteKeys?{
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                storyDatabase.remoteKeysDao().getRemoteId(id)
            }
        }
    }
    companion object{
        const val initialPageIndex = 1
    }
}