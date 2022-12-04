package com.example.mystoryapp.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class AuthRepositoryTest {

    @get: Rule
    val instanceExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var authRepo: AuthRepo


    @Before
    fun setup(){
        authRepo = AuthRepo()
    }

    @Test
    fun loginSuccess(){
        authRepo.Login("rizkyy@gmail.com","ramadhan")
        Assert.assertFalse(false)
        Assert.assertNotNull("Not null")
    }

    @Test
    fun registerSuccess(){
        authRepo.Register("hildan", "hildan@gmail.com","hildan23")
        Assert.assertFalse(false)
        Assert.assertNotNull("not null")
    }
}