package com.openclassrooms.rebonnte.data.datasource

import com.openclassrooms.rebonnte.data.dto.HistoryDto
import kotlinx.coroutines.flow.Flow

interface HistoryDataSource {

    suspend fun getHistoryById(historyId: String) : HistoryDto?
    fun getHistories() : Flow<List<HistoryDto>>
    fun getHistoryByMedicineId(medicineId: String) : Flow<List<HistoryDto>>
    suspend fun saveHistory(history: HistoryDto)
}