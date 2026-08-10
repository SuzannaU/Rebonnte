package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.service.AuthService

class LogOutUseCase(
    private val authService: AuthService
) {

    operator fun invoke() {
        authService.signOut()
    }
}