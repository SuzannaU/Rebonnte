package com.openclassrooms.rebonnte.data.datasource

import com.openclassrooms.rebonnte.data.dto.UserDto

interface UserDataSource {
    suspend fun getUserById(userId: String): UserDto?
    suspend fun saveUser(user: UserDto)
}