package com.openclassrooms.rebonnte.domain.model

data class Medicine(
    val id:String,
    val name: String,
    val aisleNumber: Int,
    val currentStock: Int,
)