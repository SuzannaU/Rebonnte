package com.openclassrooms.rebonnte.domain.service

import com.openclassrooms.rebonnte.domain.model.AuthUser
import kotlinx.coroutines.flow.Flow

interface AuthService {
    val authState: Flow<AuthUser?>
}