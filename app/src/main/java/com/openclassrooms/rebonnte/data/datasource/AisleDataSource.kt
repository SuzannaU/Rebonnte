package com.openclassrooms.rebonnte.data.datasource

import com.openclassrooms.rebonnte.data.dto.AisleDto
import kotlinx.coroutines.flow.Flow

interface AisleDataSource {
    suspend fun getAisleByNumber(number: String) : AisleDto?
    fun getAisles() : Flow<List<AisleDto>>
    suspend fun saveAisle(aisle: AisleDto)
}