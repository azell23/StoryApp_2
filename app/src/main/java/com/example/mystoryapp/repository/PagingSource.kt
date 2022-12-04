package com.example.mystoryapp.repository

import androidx.paging.PagingData
import androidx.paging.PagingState
import com.example.mystoryapp.api.Service
import com.example.mystoryapp.datastore.AuthPreference
import com.example.mystoryapp.response.ListStoryItem
import kotlinx.coroutines.flow.first

class PagingSource( private val service: Service, private val preference: AuthPreference):androidx.paging.PagingSource<Int, ListStoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {

        try {
            val position = params?.key ?: initialPageIndex
            val token = preference.getToken().first()
            val response = service.getAllStory("Bearer ${token}",params.loadSize, position)

            val listStory = response.listStory

            return LoadResult.Page( listStory,
                prevKey = if (position == initialPageIndex)null else position -1 ,
                nextKey = if (listStory.isNullOrEmpty())null else position +1)
        } catch (t:Throwable){
            return LoadResult.Error(t)
        }
    }



    companion object{
        const val initialPageIndex = 1

        fun snapshoot(items: List<ListStoryItem>): PagingData<ListStoryItem>{
            return PagingData.from(items)
        }
    }

}