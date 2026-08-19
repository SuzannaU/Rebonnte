package com.openclassrooms.rebonnte.ui.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.res.stringResource

@Immutable
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
}
