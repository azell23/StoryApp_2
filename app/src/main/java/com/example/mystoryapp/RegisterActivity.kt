package com.example.mystoryapp

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.databinding.ActivityRegisterBinding
import com.example.mystoryapp.datastore.AuthPreference
import com.example.mystoryapp.repository.AuthRepo
import com.example.mystoryapp.util.Constanta
import com.example.mystoryapp.util.PreferenceFactory
import com.example.mystoryapp.viewmodel.Auth

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: Auth
    private lateinit var repository: AuthRepo
    private lateinit var authPreference: AuthPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authPreference = AuthPreference(this)
        repository = AuthRepo()
        viewModel = ViewModelProvider(this, PreferenceFactory(authPreference,repository, this))[Auth::class.java]


        Register()
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        playAnimation()


    }

    private fun Register(){

        binding.btnRegister.setOnClickListener{
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            when {
                !email.matches(Constanta.emailPattern) -> {
                    Toast.makeText(this, "format email salah", Toast.LENGTH_SHORT).show()
                }
                email.isBlank() or username.isBlank() or password.isBlank() -> {
                    Toast.makeText(this, "Isi username, email, dan password", Toast.LENGTH_SHORT).show()
                }
                password.length < 6 ->{
                    Toast.makeText(this, "Password harus 6 karakter atau lebih", Toast.LENGTH_SHORT).show()
                } else  ->{
                    viewModel.register(username, email, password)
                    repository.register.observe(this){
                        if (it != null){
                            startActivity(Intent(this, LoginActivity::class.java))
                        } else {
                            Toast.makeText(this, "${it?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA, 1f).setDuration(500)
        val username = ObjectAnimator.ofFloat(binding.etUsername, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(login, register)
        }

        AnimatorSet().apply {
            playSequentially(email, password,username, together)
            start()
        }
    }

}