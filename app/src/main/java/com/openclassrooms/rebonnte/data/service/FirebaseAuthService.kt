package com.openclassrooms.rebonnte.data.service

import com.google.firebase.auth.FirebaseAuth
import com.openclassrooms.rebonnte.domain.exception.UserNotFoundException
import com.openclassrooms.rebonnte.domain.model.AuthUser
import com.openclassrooms.rebonnte.domain.service.AuthService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseAuthService(
    private val firebaseAuth: FirebaseAuth
) : AuthService {

    override val authState: Flow<String?> = callbackFlow {

        val listener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser
            trySend(user?.uid)
        }

        firebaseAuth.addAuthStateListener(listener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(listener)
        }
    }

    override fun getAuthUser(): AuthUser {
        val firebaseAuthUser = firebaseAuth.currentUser ?: throw UserNotFoundException("AuthUser not found")
        return AuthUser(
            uid = firebaseAuthUser.uid,
            email = firebaseAuthUser.email ?: "",
            displayName = firebaseAuthUser.displayName ?: "",
        )
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }
}