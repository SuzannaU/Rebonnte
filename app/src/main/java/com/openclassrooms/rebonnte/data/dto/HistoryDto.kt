package com.openclassrooms.rebonnte.data.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.io.Serializable

data class HistoryDto(
    @DocumentId
    val id: String = "",
    @PropertyName("medicine_id")
    val medicineId: String = "",
    @PropertyName("user_id")
    val userId: String = "",
    @PropertyName("date_time")
    val dateTime: Timestamp = Timestamp.now(),
    val details: String = "",
) : Serializable
