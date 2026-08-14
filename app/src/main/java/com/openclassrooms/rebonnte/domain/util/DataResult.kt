package com.openclassrooms.rebonnte.domain.util

import com.openclassrooms.rebonnte.data.util.toDomainException

sealed interface DataResult<out T> {
    data class Success<out T>(val data: T) : DataResult<T>
    data class Failure(val exception: Throwable) : DataResult<Nothing>
}

suspend inline fun <T> wrapDataResult(
    crossinline block: suspend () -> T
): DataResult<T> {
    return try {
        DataResult.Success(block())
    } catch (e: Exception) {
        DataResult.Failure(e.toDomainException())
    }
}
