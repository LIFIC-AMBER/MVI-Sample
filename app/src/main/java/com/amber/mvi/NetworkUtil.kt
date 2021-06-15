package com.amber.mvi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkUtil {
    private const val BASE_URL = "https://randomuser.me/"
    private fun getRetrofit() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: Api =
        getRetrofit().create(Api::class.java)
}