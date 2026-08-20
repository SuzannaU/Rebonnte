package com.openclassrooms.rebonnte.domain.service

import com.openclassrooms.rebonnte.domain.model.AuthUser
import com.openclassrooms.rebonnte.domain.util.DataResult
import kotlinx.coroutines.flow.Flow

interface AuthService {
    val authState: Flow<String?>
    suspend fun getAuthUser(): DataResult<AuthUser>
    suspend fun signOut(): DataResult<Unit>
}