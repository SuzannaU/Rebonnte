package com.openclassrooms.rebonnte.data.dto

import com.google.firebase.Timestamp

data class HistoryDto(
    val id: String,
    val medicineId: String,
    val userId: String,
    val dateTime: Timestamp,
    val details: String,
)
