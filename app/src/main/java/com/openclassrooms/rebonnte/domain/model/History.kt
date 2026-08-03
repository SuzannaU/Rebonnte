package com.openclassrooms.rebonnte.domain.model

import java.util.Date

data class History(
    val id: String = "",
    val medicineId: String,
    val userId: String,
    val dateTime: Date,
    val details: String,
)