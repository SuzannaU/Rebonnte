package com.openclassrooms.rebonnte.domain.model

import java.util.Date

data class History(
    val id: String = "",
    val medicineId: String,
    val userId: String,
    val dateTime: Date,
    val isCreation: Boolean = false,
    val isArchiving: Boolean = false,
    val updatedField: UpdatedField? = null,
)

enum class UpdatableFields {
    NAME,
    AISLE,
    STOCK,
}

data class UpdatedField(
    val field: UpdatableFields,
    val oldValue: String,
    val newValue: String,
)