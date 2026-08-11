package com.openclassrooms.rebonnte.data.repositoryImpl

import com.openclassrooms.rebonnte.data.datasource.UserDataSource
import com.openclassrooms.rebonnte.data.dto.UserDto
import com.openclassrooms.rebonnte.data.dto.toDomain
import com.openclassrooms.rebonnte.data.dto.toDto
import com.openclassrooms.rebonnte.domain.model.User
import com.openclassrooms.rebonnte.domain.repository.UserRepository

class UserRepositoryFirestoreImpl(
    private val userDataSource: UserDataSource,
) : UserRepository {
    override suspend fun getCurrentUser(): User? {
        return userDataSource.getCurrentUser()?.toDomain()
    }

    override suspend fun getUserById(userId: String): User? {
        return userDataSource.getUserById(userId)?.toDomain()
    }

    override suspend fun createUser(user: User) {
        userDataSource.saveUser(user.toDto())
    }
}