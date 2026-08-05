package com.openclassrooms.rebonnte.data.dto

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

data class MedicineDto(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val aisleNumber: String = "",
    val currentStock: Int = 0,
) : Serializable
