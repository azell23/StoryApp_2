package com.example.mystoryapp.util

import android.Manifest
import android.widget.TextView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class Constanta {
    companion object{
        const val TOKEN = "token_key"
        const val LOGIN = "auth_user_login"
        const val REQ_CODE = 23
        val REQ_PERMISSION = arrayOf(Manifest.permission.CAMERA)
        const val CAMERA_X_RESULT = 200
        const val SUCSSESS_UPLOAD = "success_upload"
        const val DETAIL = "detail"
        val emailPattern =  Regex("[a-zA-Z0-9._]+@[a-z]+\\.+[a-z]+")

        fun TextView.setLocalDate(timestamp: String){
            val simpleDate = SimpleDateFormat("yyyy-MM-dd", Locale.UK)
            val date = simpleDate.parse(timestamp) as Date
            val formatDate = DateFormat.getDateInstance(DateFormat.FULL).format(date)
            this.text = formatDate
        }


    }
}