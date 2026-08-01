package com.openclassrooms.rebonnte.domain.repository

import com.openclassrooms.rebonnte.domain.model.History
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    suspend fun getHistoryById(historyId: String) : History?
    fun getHistories() : Flow<List<History>>
    fun getHistoryByMedicineId(medicineId: String) : Flow<List<History>>
    suspend fun saveHistory(history: History)
}