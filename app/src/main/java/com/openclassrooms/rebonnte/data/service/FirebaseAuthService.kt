package com.openclassrooms.rebonnte.data.service

import com.google.firebase.auth.FirebaseAuth
import com.openclassrooms.rebonnte.domain.model.AuthUser
import com.openclassrooms.rebonnte.domain.service.AuthService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseAuthService(
    private val firebaseAuth: FirebaseAuth
) : AuthService {

    override val authState: Flow<AuthUser?> = callbackFlow {

        val listener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser?.let { AuthUser(uid = it.uid) }
            trySend(user)
        }

        firebaseAuth.addAuthStateListener(listener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(listener)
        }
    }
}