package com.openclassrooms.rebonnte.domain.useCase.user

import com.openclassrooms.rebonnte.domain.service.AuthService

class LogOutUseCase(
    private val authService: AuthService
) {

    suspend operator fun invoke() {
        authService.signOut()
    }
}