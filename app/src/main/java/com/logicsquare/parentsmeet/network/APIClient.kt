package com.logicsquare.parentsmeet.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class APIClient {
    companion object {
        private const val BASE_URL = "https://parents-api.logic-square.com/"
        val client: Retrofit
            get() {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                val client = OkHttpClient.Builder().readTimeout(1, TimeUnit.MINUTES).connectTimeout(1, TimeUnit.MINUTES).addInterceptor(interceptor).build()
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
                return retrofit
            }
    }
}