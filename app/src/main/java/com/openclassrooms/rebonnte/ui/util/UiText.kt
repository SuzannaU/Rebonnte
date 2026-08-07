package com.openclassrooms.rebonnte.ui.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UiText {
    data class RawString(val value: String) : UiText()
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any,
    ) : UiText()

    @Composable
    fun asString(): String {
        return when (this) {
            is RawString -> value
            is StringResource -> {
                val resolvedArgs = args.map { arg ->
                    if (arg is UiText) arg.asString() else arg
                }.toTypedArray()
                stringResource(resId, *resolvedArgs)
            }
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is RawString -> value
            is StringResource -> {
                val resolvedArgs = args.map { arg ->
                    if (arg is UiText) arg.asString(context) else arg
                }.toTypedArray()
                context.getString(resId, *resolvedArgs)
            }
        }
    }
}
