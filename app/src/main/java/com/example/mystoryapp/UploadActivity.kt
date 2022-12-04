package com.example.mystoryapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.databinding.ActivityUploadBinding
import com.example.mystoryapp.datastore.AuthPreference
import com.example.mystoryapp.datastore.Token
import com.example.mystoryapp.repository.AuthRepo
import com.example.mystoryapp.repository.UploadRepo
import com.example.mystoryapp.util.*
import com.example.mystoryapp.util.Constanta.Companion.CAMERA_X_RESULT
import com.example.mystoryapp.util.Constanta.Companion.REQ_CODE
import com.example.mystoryapp.util.Constanta.Companion.REQ_PERMISSION
import com.example.mystoryapp.viewmodel.Auth
import com.example.mystoryapp.viewmodel.UploadModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private val viewModel: UploadModel by viewModels{
        PreferenceFactory(authPreference,authRepository, this)
    }
    private lateinit var authModel: Auth
    private lateinit var token: Token
    private lateinit var authRepository: AuthRepo
    private lateinit var authPreference: AuthPreference
    private var getFile: File? = null
    private lateinit var repository: UploadRepo
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var location: LatLng? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQ_CODE){
            if (!allPermission()) {
                Toast.makeText(this, "Tidak mendapat perizinan", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (it){
            getLocation()
        }

    }

    private fun allPermission() = REQ_PERMISSION.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermission()){
            ActivityCompat.requestPermissions(this, REQ_PERMISSION, REQ_CODE)
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        authPreference = AuthPreference(this)
        repository = UploadRepo()
        authRepository = AuthRepo()
        token = Token(authPreference)
        authModel = ViewModelProvider(this, PreferenceFactory(authPreference,authRepository, this))[Auth::class.java]


        binding.btnCamera.setOnClickListener { cameraX() }
        binding.btnUpload.setOnClickListener { uploadStory()  }
        binding.btnGallery.setOnClickListener { gallery() }
        binding.cbLocations.setOnCheckedChangeListener{_, checked ->
            if (checked){
                getLocation()
            }
        }


    }

    private fun cameraX(){
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )

            binding.ivDetail.setImageBitmap(result)
        }
    }


    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@UploadActivity)
            getFile = myFile
            binding.ivDetail.setImageURI(selectedImg)
        }
    }
    private fun gallery(){
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Pilih Foto")
        launcherIntentGallery.launch(chooser)
    }

    @SuppressLint("missingpermission")
    private fun getLocation(){
        if (ContextCompat.checkSelfPermission(this.applicationContext,Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED )
        {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                if (it != null)
                    location = LatLng(it.latitude, it.longitude)
            }
        } else {
            Toast.makeText(this, "izinkan lokasi", Toast.LENGTH_SHORT).show()
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        }
    }
    private fun uploadStory(){
        val desc = binding.etDesc.text.toString()
        val file = reduceFileImage(getFile as File)
        val reqImage = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            reqImage
        )
        val storyDesc = binding.etDesc.text.toString().toRequestBody("text/plain".toMediaType())
        if (location != null && getFile != null && desc.isNotEmpty())
            token.getToken().observe(this){ token ->
                viewModel.uploadWithLocations(
                    "Bearer $token",
                    imageMultiPart,
                    storyDesc,
                    lat = (location as LatLng).latitude,
                    lon = (location as LatLng).longitude
                )
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Upload success ")
                builder.setPositiveButton("No"){dialog, _ ->
                    dialog.dismiss()
                }
                builder.setPositiveButton("Yes"){_, _ ->
                    this.getSharedPreferences("data",0).edit().clear()
                        .apply()
                    Intent(this, MainActivity::class.java).also {
                        startActivity(it)
                    }
                    finish()
                }
                val alert = builder.create()
                alert.show()
            }
        else if (getFile != null && desc != null)
            token.getToken().observe(this) { token ->
                viewModel.upload("Bearer $token", imageMultiPart, storyDesc)
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Upload success ")
                builder.setPositiveButton("No"){dialog, _ ->
                    dialog.dismiss()
                }
                builder.setPositiveButton("Yes"){_, _ ->
                    this.getSharedPreferences("data",0).edit().clear()
                        .apply()
                    Intent(this, MainActivity::class.java).also {
                        startActivity(it)
                    }
                    finish()
                }
                val alert = builder.create()
                alert.show()
            }
        else Toast.makeText(this, "Tambahkan deskripsi atau gambar", Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}