package com.example.mystoryapp.data

import com.example.mystoryapp.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call

object Dummy {

    fun DummyListStory(): Story{
        val data = ArrayList<ListStoryItem>()
        for (i in 0 .. 200){
            val list = ListStoryItem(
                "link",
                "2002-10-11",
                "hildan",
                "SIB",
                "$i",
                -6.72818,
                10.0020
            )
            data.add(list)
        }
        return Story(data, false, "success")
    }
    fun dummyStory(): List<ListStoryItem>{
        val data :MutableList<ListStoryItem> = arrayListOf()

        for (i in 0 .. 200){
            val list = ListStoryItem(
                "link",
                "2002-10-11",
                "hildan",
                "SIB",
                "$i",
                -6.72818,
                10.0020
            )
            data.add(list)
        }
        return data
    }

    fun dummyDesc() : RequestBody {
        val text ="hildan"
        return text.toRequestBody()
    }
    fun dummyImage() : MultipartBody.Part {
        val text ="hildan"
        return MultipartBody.Part.create(text.toRequestBody())
    }

    fun loginResult(): Login {
        return Login(
            LoginResult(
                userId = "user---1oNKEITAsbTeMn",
                name = "Arab",
                token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLS0tMW9OS0VJVEFzYlRlTW4iLCJpYXQiOjE2NjkyNDI2NDl9.l_VL_I-TxoHVgYqAJB4HFDWIw8YCd0-9FfGedwQA808"
            ),
            false, "success"
        )
    }
    fun register(): Register{
        return Register(
            error = false,
            message = "success"
        )
    }
    fun uploadStory(): UploadStory{
        return UploadStory(
            error = false,
            message = "success"
        )
    }
    fun uploadStoryWithLocation(): Story{
        return Story(
            error = false,
            message = "success",
            listStory = dummyStory()
        )
    }
    fun Story(): Story{
        return Story(
            dummyStory(),
            error = false,
            message = "success"
        )
    }
}