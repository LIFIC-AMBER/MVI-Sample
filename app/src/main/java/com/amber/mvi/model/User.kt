package com.amber.mvi.model

data class User(
    val name: UserName,
    val email: String
)

data class UserName(val first: String, val last: String)
data class UserDTO(val results: List<User>)