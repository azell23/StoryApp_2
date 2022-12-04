package com.example.mystoryapp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.util.Constanta.Companion.LOGIN
import com.example.mystoryapp.util.Constanta.Companion.TOKEN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


private val Context.loginAuth: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(name = LOGIN)


class AuthPreference (private val context: Context) {

    private val tokenKey = stringPreferencesKey(TOKEN)

    suspend fun setToken(token: String){
        context.loginAuth.edit {
            it[tokenKey] = token
        }
    }

    suspend fun deleteToken(){
        context.loginAuth.edit {
            it.clear()
        }
    }

    fun getToken(): Flow<String> {
        return context.loginAuth.data.map {
            it[tokenKey] ?: "notfound"
        }
    }

}

class Token (private val authPreference: AuthPreference): ViewModel(){

    fun setToken(token: String){
        viewModelScope.launch {
            authPreference.setToken(token)
        }
    }
    fun deleteToken(){
        viewModelScope.launch {
            authPreference.deleteToken()
        }
    }

    fun getToken() : LiveData<String> = authPreference.getToken().asLiveData()

}