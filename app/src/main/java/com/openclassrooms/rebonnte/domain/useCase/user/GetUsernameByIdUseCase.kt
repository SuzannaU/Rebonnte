package com.openclassrooms.rebonnte.domain.useCase.user

import com.openclassrooms.rebonnte.domain.repository.UserRepository
import com.openclassrooms.rebonnte.domain.util.DataResult

class GetUsernameByIdUseCase(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(userId: String): DataResult<String?> {
        return when (val result = userRepository.getUserById(userId = userId)) {

            is DataResult.Success -> DataResult.Success(result.data?.username)
            is DataResult.Failure -> result
        }
    }
}