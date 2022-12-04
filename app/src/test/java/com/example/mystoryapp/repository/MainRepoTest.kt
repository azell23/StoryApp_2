package com.example.mystoryapp.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.example.mystoryapp.adapter.Main
import com.example.mystoryapp.data.Dummy
import com.example.mystoryapp.data.MainDispatcherRule
import com.example.mystoryapp.data.getOrAwait
import com.example.mystoryapp.response.ListStoryItem
import com.example.mystoryapp.viewmodel.MainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainRepoTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var repository: MainRepo

    @Before
    fun setup(){

    }

    @Test
    fun getStoryReturnSuccess() = runTest {
        val dummy = Dummy.dummyStory()
        val data = PagingSource.snapshoot(dummy)
        val viewModel = MainModel(repository)


        val expectedData = MutableLiveData<PagingData<ListStoryItem>>()
        expectedData.value = data

        `when`(repository.getAllStory()).thenReturn(expectedData)
        viewModel.getAllStory()
        val actualData = viewModel.getAllStory().getOrAwait()

        val differ = AsyncPagingDataDiffer(
            diffCallback = Main.DIFF_CALBACk,
            updateCallback = Main.noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualData)

        assertNotNull(differ.snapshot())
        assertEquals(dummy,differ.snapshot())
        assertEquals(dummy.size,differ.snapshot().size)
        assertEquals(dummy[0].id, differ.snapshot()[0]?.id)

    }



}