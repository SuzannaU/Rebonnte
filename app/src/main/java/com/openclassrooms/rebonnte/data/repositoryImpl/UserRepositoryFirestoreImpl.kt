package com.openclassrooms.rebonnte.data.repositoryImpl

import com.openclassrooms.rebonnte.data.datasource.UserDataSource
import com.openclassrooms.rebonnte.data.util.toDomain
import com.openclassrooms.rebonnte.data.util.toDto
import com.openclassrooms.rebonnte.data.util.wrapDataResult
import com.openclassrooms.rebonnte.domain.model.User
import com.openclassrooms.rebonnte.domain.repository.UserRepository
import com.openclassrooms.rebonnte.domain.util.DataResult

class UserRepositoryFirestoreImpl(
    private val userDataSource: UserDataSource,
) : UserRepository {

    override suspend fun getUserById(userId: String): DataResult<User?> {
        return wrapDataResult {
            userDataSource.getUserById(userId)?.toDomain()
        }
    }

    override suspend fun createUser(user: User): DataResult<Unit> {
        return wrapDataResult {
            userDataSource.saveUser(user.toDto())
        }
    }
}