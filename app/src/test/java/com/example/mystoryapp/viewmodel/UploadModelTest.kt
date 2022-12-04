package com.example.mystoryapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.mystoryapp.data.Dummy
import com.example.mystoryapp.data.MainDispatcherRule
import com.example.mystoryapp.data.getOrAwait
import com.example.mystoryapp.repository.UploadRepo
import com.example.mystoryapp.response.UploadStory
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UploadModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    @Mock
    private lateinit var repository : UploadRepo
    private lateinit var viewModel : UploadModel
    private val dummy = Dummy.uploadStory()
    private val token = "token"
    private val image = Dummy.dummyImage()
    private val desc = Dummy.dummyDesc()

    @Before
    fun setup(){
        viewModel = UploadModel(repository)
    }
    @Test
    fun addStorySuccess() = runTest {
        val expectedData = MutableLiveData<UploadStory>()
        expectedData.postValue(dummy)
        `when`(repository.uploadStory(token, image, desc)).thenReturn(expectedData)
        val actualData = viewModel.upload(token, image, desc).getOrAwait()
        Mockito.verify(repository).uploadStory(token, image, desc)
        assertNotNull(actualData)
        assertEquals(actualData, expectedData.value)
    }
    @Test
    fun addStoryWithLocationSuccess() = runTest {
        val location = LatLng(10.10,0.0)
        val expectedData = MutableLiveData<UploadStory>()
        expectedData.postValue(dummy)
        `when`(repository.uploadStoryWithLocation(token, image, desc,location.latitude,location.longitude)).thenReturn(expectedData)
        val actualData = viewModel.uploadWithLocations(token, image, desc,location.latitude,location.longitude).getOrAwait()
        Mockito.verify(repository).uploadStoryWithLocation(token, image, desc,location.latitude,location.longitude)
        assertNotNull(actualData)
        assertEquals(actualData, expectedData.value)
    }

}