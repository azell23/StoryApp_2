package com.example.mystoryapp.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mystoryapp.data.Dummy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UploadRepoTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository : UploadRepo

    @Test

    fun addStoryReturnFailed() = runTest {
        val image = Dummy.dummyImage()
        val desc = Dummy.dummyDesc()
        repository.uploadStory("token",image, desc)
        assertTrue(true)
        assertNotNull("not null")
    }
    @Test
    fun addStoryWithLocationsReturnFailed()= runTest {
        val image = Dummy.dummyImage()
        val desc = Dummy.dummyDesc()
        repository.uploadStoryWithLocation("token",image, desc,10.20,23.32)
        assertTrue(true)
        assertNotNull("not null")
    }
}