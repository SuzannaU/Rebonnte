package com.openclassrooms.rebonnte.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class MedicineUi(
    val id: String,
    val name: String,
    val aisleNumber: String,
    val currentStock: String,
)
