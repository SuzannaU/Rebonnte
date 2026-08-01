package com.openclassrooms.rebonnte.domain.repository

import com.openclassrooms.rebonnte.domain.model.User

interface UserRepository {
    suspend fun getCurrentUser(): User?
    suspend fun getUserById(userId: String): User?
    suspend fun createUser()
    fun signOut()
}