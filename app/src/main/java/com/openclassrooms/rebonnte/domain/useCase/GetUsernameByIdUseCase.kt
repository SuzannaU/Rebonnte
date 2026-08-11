package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.repository.UserRepository

class GetUsernameByIdUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(userId: String): String {
        if(userId.isBlank()) return "Unknown user"
        return userRepository.getUserById(userId = userId)?.username ?: "No username"
    }
}