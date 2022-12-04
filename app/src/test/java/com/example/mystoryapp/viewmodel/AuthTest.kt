package com.example.mystoryapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.mystoryapp.data.Dummy
import com.example.mystoryapp.data.MainDispatcherRule
import com.example.mystoryapp.data.getOrAwait
import com.example.mystoryapp.datastore.AuthPreference
import com.example.mystoryapp.repository.AuthRepo
import com.example.mystoryapp.response.Login
import com.example.mystoryapp.response.Register
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class AuthTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var repository: AuthRepo
    private lateinit var preference: AuthPreference
    private lateinit var viewModel: Auth
    private val dummyLogin = Dummy.loginResult()
    private val dummyRegister = Dummy.register()
    private val dummyEmail = "rizky@gmail.com"
    private val dummyPassword = "ramadhan"


    @Before
    fun setup(){
        preference = AuthPreference(Mockito.mock(android.content.Context::class.java))
        viewModel = Auth(repository)
    }

    @After
    fun tearDown(){
    }

    @Test
    fun loginSuccess() = runTest {
        val expectedData = MutableLiveData<Login>()
        expectedData.value = dummyLogin
        Mockito.`when`(repository.Login(dummyEmail, dummyPassword)).thenReturn(expectedData)

        val actualData = viewModel.login(dummyEmail,dummyPassword).getOrAwait()
        Mockito.verify(repository).Login(dummyEmail,dummyPassword)
        assertNotNull(actualData)
        assertEquals(expectedData.value, actualData)
    }
    @Test
    fun registerSuccess() = runTest {
        val expectedData = MutableLiveData<Register>()
        expectedData.value = dummyRegister
        Mockito.`when`(repository.Register("ramadhan",dummyEmail, dummyPassword)).thenReturn(expectedData)

        val actualData = viewModel.register("ramadhan",dummyEmail,dummyPassword).getOrAwait()
        Mockito.verify(repository).Register("ramadhan",dummyEmail,dummyPassword)
        assertNotNull(actualData)
        assertEquals(expectedData.value, actualData)
    }
}