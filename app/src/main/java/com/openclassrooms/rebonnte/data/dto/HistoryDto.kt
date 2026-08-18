package com.openclassrooms.rebonnte.data.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.openclassrooms.rebonnte.domain.model.UpdatableFields
import java.io.Serializable

data class HistoryDto(
    @DocumentId
    val id: String = "",
    val medicineId: String = "",
    val userId: String = "",
    val dateTime: Timestamp = Timestamp.now(),
    val creation: Boolean = false,
    val archiving: Boolean = false,
    val updatedField: UpdatedFieldDto? = null,
) : Serializable

data class UpdatedFieldDto(
    val field: UpdatableFields = UpdatableFields.NAME,
    val oldValue: String = "",
    val newValue: String = "",
)
