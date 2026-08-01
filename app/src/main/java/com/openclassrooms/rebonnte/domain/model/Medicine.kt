package com.openclassrooms.rebonnte.domain.model

data class Medicine(
    val id:String,
    val name: String,
    val aisleId: String,
    val currentStock: Int,
)