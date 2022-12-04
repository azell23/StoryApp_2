package com.example.mystoryapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.example.mystoryapp.adapter.Main
import com.example.mystoryapp.data.Dummy
import com.example.mystoryapp.data.MainDispatcherRule
import com.example.mystoryapp.data.getOrAwait
import com.example.mystoryapp.repository.MainRepo
import com.example.mystoryapp.repository.PagingSource
import com.example.mystoryapp.response.ListStoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainModelTest{

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var repository: MainRepo
    private lateinit var viewModel : MainModel

    @Before
    fun setup(){
        viewModel = MainModel(repository)

    }

    @Test
    fun getStoryReturnSuccess() = runTest {
        val dummy = Dummy.dummyStory()
        val data = PagingSource.snapshoot(dummy)



        val expectedData = MutableLiveData<PagingData<ListStoryItem>>()
        expectedData.value = data

        Mockito.`when`(viewModel.getAllStory()).thenReturn(expectedData)
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