package com.openclassrooms.rebonnte.data.dto

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

data class AisleDto(
    @DocumentId
    val id: String = "",    // Aisle number
) : Serializable
