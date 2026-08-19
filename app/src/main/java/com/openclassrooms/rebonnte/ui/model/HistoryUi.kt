package com.openclassrooms.rebonnte.ui.model

import androidx.compose.runtime.Immutable
import com.openclassrooms.rebonnte.ui.util.UiText

@Immutable
data class HistoryUi(
    val medicineId: String,
    val username: UiText,
    val dateTime: String,
    val details: UiText = UiText.RawString(""),
)
