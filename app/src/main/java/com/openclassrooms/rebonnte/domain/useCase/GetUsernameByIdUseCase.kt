package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.repository.UserRepository
import com.openclassrooms.rebonnte.domain.util.DataResult

class GetUsernameByIdUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(userId: String): String {
        if(userId.isBlank()) return "Unknown user"
        return when (val result = userRepository.getUserById(userId = userId)) {
            is DataResult.Success -> result.data?.username ?: "No username"
            is DataResult.Failure -> "Unknown user"
        }
    }
}