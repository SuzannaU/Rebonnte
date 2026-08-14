package com.openclassrooms.rebonnte.domain.repository

import com.openclassrooms.rebonnte.domain.model.User
import com.openclassrooms.rebonnte.domain.util.DataResult

interface UserRepository {
    suspend fun getUserById(userId: String): DataResult<User?>
    suspend fun createUser(user: User): DataResult<Unit>
}