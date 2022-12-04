package com.example.mystoryapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mystoryapp.api.Config
import com.example.mystoryapp.response.Login
import com.example.mystoryapp.response.LoginResult
import com.example.mystoryapp.response.Register
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AuthRepo {

    private val _Login = MutableLiveData<LoginResult>()
    val _login = MutableLiveData<Login>()
    val login : LiveData<LoginResult> = _Login
    private val _register = MutableLiveData<Register>()
    val register : LiveData<Register> = _register



    fun Login(email : String, password: String): LiveData<Login> {
        Config.getApiService().login(email, password)
            .enqueue(object : Callback<Login> {
                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    if (response.isSuccessful){
                        response.body().let { loginResult ->
                            loginResult?.loginResult?.let {
                                _Login.value = LoginResult(it.name, it.userId, it.token)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<Login>, t: Throwable) {
                }

            })
        return _login
    }
    fun Register(name: String, email:String, password: String): LiveData<Register> {
        Config.getApiService().register(name, email, password)
            .enqueue(object : Callback<Register> {
                override fun onResponse(call: Call<Register>, response: Response<Register>) {
                    if (response.isSuccessful){
                        _register.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<Register>, t: Throwable) {
                }

            })
        return _register
    }
}