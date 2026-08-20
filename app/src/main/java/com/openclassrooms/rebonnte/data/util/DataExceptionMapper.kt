package com.openclassrooms.rebonnte.data.util

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.openclassrooms.rebonnte.domain.exception.AuthException
import com.openclassrooms.rebonnte.domain.exception.DatabaseException
import com.openclassrooms.rebonnte.domain.exception.NetworkException
import com.openclassrooms.rebonnte.domain.exception.UnknownException
import com.openclassrooms.rebonnte.domain.util.DataResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map


suspend inline fun <T> wrapDataResult(
    crossinline block: suspend () -> T
): DataResult<T> {
    return try {
        DataResult.Success(block())
    } catch (e: Exception) {
        DataResult.Failure(e.toDomainException())
    }
}

fun <T> Flow<T>.asDataResult(): Flow<DataResult<T>> {
    return this
        .map { DataResult.Success(it) as DataResult<T> }
        .catch { emit(DataResult.Failure(it.toDomainException())) }
}

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
