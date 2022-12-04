package com.example.mystoryapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.databinding.ActivityLoginBinding
import com.example.mystoryapp.datastore.AuthPreference
import com.example.mystoryapp.datastore.Token
import com.example.mystoryapp.repository.AuthRepo
import com.example.mystoryapp.util.Constanta
import com.example.mystoryapp.util.PreferenceFactory
import com.example.mystoryapp.viewmodel.Auth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: Auth
    private lateinit var repository: AuthRepo
    private lateinit var token: Token
    private lateinit var authPreference: AuthPreference
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authPreference = AuthPreference(this)
        token = Token(authPreference)
        repository = AuthRepo()
        viewModel = ViewModelProvider(this, PreferenceFactory(authPreference,repository, this))[Auth::class.java]



        Login()
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

    }

    private fun Login(){
        binding.btnLogin.setOnClickListener{
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            when {
                !email.matches(Constanta.emailPattern) -> {
                    Toast.makeText(this, "Format email salah", Toast.LENGTH_SHORT).show()
                }
                email.isBlank() or password.isBlank() -> {
                    Toast.makeText(this, "Isi email dan password", Toast.LENGTH_SHORT).show()
                } else -> {
                    viewModel.login(email, password)
                    repository.login.observe(this){
                        token.setToken(it.token)
                        startActivity(Intent(this, MainActivity::class.java))
                        Toast.makeText(this, "Selamat datang di Story App ${it.name}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}