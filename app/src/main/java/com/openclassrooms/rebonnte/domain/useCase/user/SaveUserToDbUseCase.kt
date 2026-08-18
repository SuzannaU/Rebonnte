package com.openclassrooms.rebonnte.domain.useCase.user

import com.openclassrooms.rebonnte.domain.model.User
import com.openclassrooms.rebonnte.domain.repository.UserRepository
import com.openclassrooms.rebonnte.domain.util.DataResult

class SaveUserToDbUseCase(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(user: User) : DataResult<Unit> {
        return userRepository.createUser(user)
    }
}