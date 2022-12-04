package com.example.mystoryapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.adapter.Loading
import com.example.mystoryapp.adapter.Main
import com.example.mystoryapp.database.Story
import com.example.mystoryapp.databinding.ActivityMainBinding
import com.example.mystoryapp.datastore.AuthPreference
import com.example.mystoryapp.datastore.Token
import com.example.mystoryapp.repository.AuthRepo
import com.example.mystoryapp.util.PreferenceFactory
import com.example.mystoryapp.viewmodel.Auth
import com.example.mystoryapp.viewmodel.MainModel
import com.example.mystoryapp.viewmodel.Paging
import com.google.android.gms.maps.model.LatLng
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapterMain : Main
    private val viewModel: MainModel by viewModels {
        PreferenceFactory(authPreference,authRepository, this)
    }
    private lateinit var pagingModel: Paging
    private lateinit var authPreference: AuthPreference
    private lateinit var authViewModel: Auth
    private lateinit var token: Token
    private lateinit var authRepository: AuthRepo
    private var listMap :ArrayList<LatLng>? = null
    private var listMapName: ArrayList<String>? = null
    private lateinit var database: Story

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Story.getInstance(this)
        adapterMain = Main()

        authPreference = AuthPreference(this)
        authRepository = AuthRepo()
        token = Token(authPreference)
        authViewModel = ViewModelProvider(this, PreferenceFactory(authPreference,authRepository,this ))[Auth::class.java]
        pagingModel = ViewModelProvider(this, PreferenceFactory(authPreference,authRepository,this))[Paging::class.java]


        token.getToken().observe(this){
            if (it != null){
                setRecycle()
                fetchData()
            }else {
                Log.d("TOKEN", "null")
            }
        }
        upload()
        refresh()

        pagingModel.getStory.observe(this){ listStory ->
            Log.d("cek data", "$listStory")
            adapterMain.submitData(lifecycle, listStory)

        }
    }


    private fun refresh(){
        binding.refresh.setOnRefreshListener { onRefresh() }
        viewModel.loading.observe(this){
            loading(it)
        }
    }
    private fun upload(){
        binding.fabUpload.setOnClickListener {
            startActivity(Intent(this, UploadActivity::class.java))
        }
    }



    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
    private fun setRecycle(){
        adapterMain = Main()
        binding.recyclerViewMain.adapter = adapterMain
        binding.recyclerViewMain.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.recyclerViewMain.smoothScrollToPosition(0)
        binding.recyclerViewMain.apply {
            adapter =adapterMain.withLoadStateFooter(
                footer = Loading{
                    adapterMain.retry()
                }
            )

        }
    }
    private fun fetchData(){
        viewModel.getAllStory().observe(this){
            adapterMain.submitData(lifecycle,it)
            adapterMain.snapshot().items
        }
    }
    private fun onRefresh(){
        binding.refresh.isRefreshing = true
        Timer().schedule(1500){

            binding.refresh.isRefreshing = false
        }
        binding.recyclerViewMain.smoothScrollBy(0,0)
        startActivity(Intent(this, MainActivity::class.java))
    }
    private fun loading(_loading: Boolean){
        binding.pb.visibility = if (_loading) View.VISIBLE else View.GONE
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Do You want logout ?")
                builder.setPositiveButton("No"){dialog, _ ->
                    dialog.dismiss()
                }
                builder.setPositiveButton("Yes"){_, _ ->
                    this.getSharedPreferences("data",0).edit().clear()
                        .apply()
                    Intent(this, LoginActivity::class.java).also {
                        token.deleteToken()
                        startActivity(it)
                    }
                    finish()
                }
                val alert = builder.create()
                alert.show()
            }
            R.id.maps -> {
                startActivity(Intent(this@MainActivity, MapsActivity::class.java).also {
                    it.putExtra(LIST_MAP,listMap)
                    it.putExtra(LIST_MAP_NAME,listMapName)
                })

                }
            }
        return super.onOptionsItemSelected(item)
    }
    companion object{
        const val LIST_MAP = "list_map"
        const val LIST_MAP_NAME= "list_map_name"
    }
}

