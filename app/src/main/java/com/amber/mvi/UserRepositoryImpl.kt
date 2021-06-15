package com.amber.mvi

import com.amber.mvi.model.User
import com.amber.mvi.model.UserDTO

class UserRepositoryImpl(val api: Api): UserRepository {
    override suspend fun getUsers(): UserDTO = api.getUsers()
}