package com.openclassrooms.rebonnte.data.dto

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

data class AisleDto(
    @DocumentId
    val id: String = "",
    val number: Int = 9999,
) : Serializable
