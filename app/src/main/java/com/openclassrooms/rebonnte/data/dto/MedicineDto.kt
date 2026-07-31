package com.openclassrooms.rebonnte.data.dto

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.io.Serializable

data class MedicineDto(
    @DocumentId
    val id: String = "",
    val name: String = "",
    @PropertyName("aisle_id")
    val aisleId: String = "",
    @PropertyName("current_stock")
    val currentStock: Int = 0,
) : Serializable
