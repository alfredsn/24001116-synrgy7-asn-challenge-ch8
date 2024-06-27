package com.example.data.datasource.remote

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ServiceApi {
    @Headers(
        "x-rapidapi-key: ab924f807dmsh29a5dc04a83de07p1fcd35jsn06dca166209b",
        "x-rapidapi-host: mail-sender-api1.p.rapidapi.com",
        "Content-Type: application/json"
    )
    @POST("/")
    fun sendEmail(@Body requestBody: RequestBody): Call<Any>
}