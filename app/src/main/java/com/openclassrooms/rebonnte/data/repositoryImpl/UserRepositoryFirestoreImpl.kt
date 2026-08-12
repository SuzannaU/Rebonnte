package com.openclassrooms.rebonnte.data.repositoryImpl

import com.openclassrooms.rebonnte.data.datasource.UserDataSource
import com.openclassrooms.rebonnte.data.dto.toDomain
import com.openclassrooms.rebonnte.data.dto.toDto
import com.openclassrooms.rebonnte.data.util.toDomainException
import com.openclassrooms.rebonnte.domain.model.User
import com.openclassrooms.rebonnte.domain.repository.UserRepository
import com.openclassrooms.rebonnte.domain.util.DataResult
import com.openclassrooms.rebonnte.domain.util.wrapDataResult

class UserRepositoryFirestoreImpl(
    private val userDataSource: UserDataSource,
) : UserRepository {

    override suspend fun getCurrentUser(): DataResult<User?> {
        return wrapDataResult(onError = { it.toDomainException() }) {
            userDataSource.getCurrentUser()?.toDomain()
        }
    }

    override suspend fun getUserById(userId: String): DataResult<User?> {
        return wrapDataResult(onError = { it.toDomainException() }) {
            userDataSource.getUserById(userId)?.toDomain()
        }
    }

    override suspend fun createUser(user: User): DataResult<Unit> {
        return wrapDataResult(onError = { it.toDomainException() }) {
            userDataSource.saveUser(user.toDto())
        }
    }
}