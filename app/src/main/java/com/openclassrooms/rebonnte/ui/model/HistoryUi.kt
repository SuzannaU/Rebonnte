package com.openclassrooms.rebonnte.ui.model

import com.openclassrooms.rebonnte.ui.util.UiText

data class HistoryUi(
    val medicineId: String,
    val username: UiText,
    val dateTime: String,
    val details: UiText = UiText.RawString(""),
)
