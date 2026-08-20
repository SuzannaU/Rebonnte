package com.openclassrooms.rebonnte.domain.util

sealed interface DataResult<out T> {
    data class Success<out T>(val data: T) : DataResult<T>
    data class Failure(val exception: Throwable) : DataResult<Nothing>
}