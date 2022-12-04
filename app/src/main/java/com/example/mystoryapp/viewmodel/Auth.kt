package com.example.mystoryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.datastore.AuthPreference
import com.example.mystoryapp.repository.AuthRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Auth @Inject constructor(private val authRepo: AuthRepo): ViewModel() {

    fun login(email: String, password:String) =
        authRepo.Login(email, password)
    fun register(username:String, email: String, password: String) =
        authRepo.Register(username,email, password)





}