package com.openclassrooms.rebonnte.domain.repository

import com.openclassrooms.rebonnte.domain.model.Aisle
import kotlinx.coroutines.flow.Flow

interface AisleRepository {
    suspend fun getAisleById(aisleId: String): Aisle?
    fun getAisles(): Flow<List<Aisle>>
    suspend fun addAisle(aisle: Aisle)
}