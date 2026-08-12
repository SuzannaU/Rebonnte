package com.openclassrooms.rebonnte.data.util

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.openclassrooms.rebonnte.domain.exception.AuthException
import com.openclassrooms.rebonnte.domain.exception.DatabaseException
import com.openclassrooms.rebonnte.domain.exception.NetworkException
import com.openclassrooms.rebonnte.domain.exception.UnknownException

fun Throwable.toDomainException(): Throwable {
    return when (this) {
        is FirebaseFirestoreException -> {
            when (this.code) {
                FirebaseFirestoreException.Code.UNAVAILABLE,
                FirebaseFirestoreException.Code.DEADLINE_EXCEEDED -> NetworkException(
                    this.message ?: "Network error"
                )

                else -> DatabaseException(this.message ?: "Database error")
            }
        }

        is FirebaseAuthException -> AuthException(this.message ?: "Auth error")

        is FirebaseNetworkException -> NetworkException(
            this.message ?: "Network error"
        )

        else -> UnknownException(this.message ?: "Unknown Exception")
    }
}
