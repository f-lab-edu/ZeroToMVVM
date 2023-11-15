package com.kova700.zerotomvvm.data.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kova700.zerotomvvm.util.BASE_URL
import com.kova700.zerotomvvm.util.JSON_MEDIA_TYPE
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object RetrofitBuilder {

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getHttpClient())
            .addConverterFactory(Json.asConverterFactory(JSON_MEDIA_TYPE.toMediaType()))
            .build()
    }

    private fun getHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
            ).build()
    }
}