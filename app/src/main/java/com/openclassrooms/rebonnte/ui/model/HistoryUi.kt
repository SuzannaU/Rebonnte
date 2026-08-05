package com.openclassrooms.rebonnte.ui.model

data class HistoryUi(
    val medicineId: String,
    val userId: String,
    val dateTime: String,
    val details: String = "",
)
