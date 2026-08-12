package com.openclassrooms.rebonnte.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.openclassrooms.rebonnte.data.dto.UserDto
import com.openclassrooms.rebonnte.domain.service.AuthService
import kotlinx.coroutines.tasks.await

private const val USER_COLLECTION = "users"

class UserFirestoreDataSource(
    private val firestore: FirebaseFirestore,
    private val authService: AuthService
) : UserDataSource {

    override suspend fun getCurrentUser(): UserDto? {
        val authUser = authService.getAuthUser()
        val uid = authUser.uid
        return firestore.collection(USER_COLLECTION).document(uid)
            .get()
            .await()
            .toObject<UserDto>()
    }

    override suspend fun getUserById(userId: String): UserDto? {
        return firestore.collection(USER_COLLECTION).document(userId)
            .get()
            .await()
            .toObject<UserDto>()
    }

    override suspend fun saveUser(user: UserDto) {
        firestore.collection(USER_COLLECTION).document(user.id)
            .set(user).await()
    }
}