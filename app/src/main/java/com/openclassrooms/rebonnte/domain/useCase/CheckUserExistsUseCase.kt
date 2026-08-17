package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.repository.UserRepository
import com.openclassrooms.rebonnte.domain.util.DataResult

class CheckUserExistsUseCase(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(userId: String) : DataResult<Boolean> {
        return when (val userResult = userRepository.getUserById(userId)) {
            is DataResult.Success -> DataResult.Success(userResult.data != null)
            is DataResult.Failure -> DataResult.Failure(userResult.exception)
        }
    }
}