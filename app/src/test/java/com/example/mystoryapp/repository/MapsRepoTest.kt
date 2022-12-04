package com.example.mystoryapp.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.mystoryapp.data.Dummy
import com.example.mystoryapp.response.ListStoryItem
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapsRepoTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository : MapsRepo

    @Test
    fun getStoryReturnSuccess()= runTest {
        val observer = Observer<List<ListStoryItem>>{}
        val dummy = Dummy.dummyStory()

        try {
            val expectedData = MutableLiveData<List<ListStoryItem>>()
            expectedData.value = dummy

            `when`(repository.getStoryWithMap("token")).thenReturn(expectedData)

            val actualData = repository.getStoryWithMap("token").observeForever(observer)
            Mockito.verify(repository).getStoryWithMap("token")
            assertNotNull(actualData)
        } finally {
            repository.getStoryWithMap("token").removeObserver(observer)
        }
    }
}