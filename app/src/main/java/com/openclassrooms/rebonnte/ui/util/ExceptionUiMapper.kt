package com.openclassrooms.rebonnte.ui.util

import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.domain.exception.AuthException
import com.openclassrooms.rebonnte.domain.exception.DatabaseException
import com.openclassrooms.rebonnte.domain.exception.NetworkException

fun Throwable.toErrorMessageId(): Int {
    return when (this) {
        is NetworkException -> R.string.network_error
        is DatabaseException -> R.string.database_error
        is AuthException -> R.string.auth_error
        else -> R.string.unknown_error
    }
}
