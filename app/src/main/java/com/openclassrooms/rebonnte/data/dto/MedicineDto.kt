package com.openclassrooms.rebonnte.data.dto

data class MedicineDto(
    val id: String,
    val name: String,
    val aisleNumber: Int,
    val currentStock: Int = 0,
)
