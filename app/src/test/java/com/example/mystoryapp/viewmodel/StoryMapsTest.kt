package com.example.mystoryapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.mystoryapp.data.Dummy
import com.example.mystoryapp.data.getOrAwait
import com.example.mystoryapp.repository.MapsRepo
import com.example.mystoryapp.response.ListStoryItem
import com.example.mystoryapp.response.Story
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StoryMapsTest{

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository : MapsRepo
    private lateinit var viewModel : StoryMaps
    private val dummy = Dummy.dummyStory()

    @Before
    fun setup(){
        viewModel = StoryMaps(repository)
    }
    @Test
    fun getStoryWithMapsReturnSuccess()= runTest {

        val expectedData = MutableLiveData<List<ListStoryItem>>()
        expectedData.postValue( dummy)
        `when`(repository.getStoryWithMap("token")).thenReturn(expectedData)
        val actualData = viewModel.getStoryWithMaps("token").getOrAwait()
        Mockito.verify(repository).getStoryWithMap("token")
        assertNotNull(actualData)
        assertEquals(actualData,expectedData.value)
    }
    @Test
    fun getMarkerMapsReturnSuccess()= runTest {

        val expectedData = MutableLiveData<List<ListStoryItem>>()
        expectedData.postValue( dummy)
        `when`(repository.getStory()).thenReturn(expectedData)
        val actualData = viewModel.getStory().getOrAwait()
        Mockito.verify(repository).getStory()
        assertNotNull(actualData)
        assertEquals(actualData,expectedData.value)
    }
}