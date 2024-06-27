package com.example.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.datasource.remote.ClientApi
import com.example.domain.model.EmailData
import com.example.domain.repository.MainRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainRepositoryImpl : MainRepository {
    private val mailSenderApi = ClientApi.api

    override fun sendEmail(emailData: EmailData): LiveData<Result<Any?>> {
        val result = MutableLiveData<Result<Any?>>()

        val json = """
            {
                "sendto": "${emailData.sendTo}",
                "name": "${emailData.name}",
                "replyTo": "${emailData.replyTo}",
                "ishtml": "${emailData.isHtml}",
                "title": "${emailData.title}",
                "body": "${emailData.body}"
            }
        """.trimIndent()

        val mediaType = "application/json".toMediaTypeOrNull()
        val requestBody = json.toRequestBody(mediaType)

        mailSenderApi.sendEmail(requestBody).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    result.postValue(Result.success(response.body()))
                } else {
                    result.postValue(Result.failure(Exception("Email sending failed")))
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                result.postValue(Result.failure(t))
            }
        })

        return result
    }
}