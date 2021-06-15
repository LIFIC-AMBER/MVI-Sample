package com.amber.mvi

import com.amber.mvi.model.User
import com.amber.mvi.model.UserDTO

interface UserRepository {
    suspend fun getUsers(): UserDTO
}