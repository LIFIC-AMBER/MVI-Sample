package com.amber.mvi

import com.amber.mvi.model.User
import com.amber.mvi.model.UserDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("api/")
    suspend fun getUsers(): UserDTO
}