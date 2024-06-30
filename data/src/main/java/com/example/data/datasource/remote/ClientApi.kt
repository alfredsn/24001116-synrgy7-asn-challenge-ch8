package com.example.data.datasource.remote

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ClientApi {
    private lateinit var retrofit: Retrofit

    fun init(context: Context) {
        val okHttpClient = createOkHttpClient(context)

        retrofit = Retrofit.Builder()
            .baseUrl("https://mail-sender-api1.p.rapidapi.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun createOkHttpClient(context: Context): OkHttpClient {
        val chuckerInterceptor = ChuckerInterceptor.Builder(context)
            .collector(ChuckerCollector(context))
            .maxContentLength(250_000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(false)
            .build()

        return OkHttpClient.Builder()
            .addInterceptor(chuckerInterceptor)
            .build()
    }

    val api: ServiceApi by lazy { retrofit.create(ServiceApi::class.java) }
}