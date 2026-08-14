package com.openclassrooms.rebonnte.data.datasource

import com.openclassrooms.rebonnte.data.dto.HistoryDto
import kotlinx.coroutines.flow.Flow

interface HistoryDataSource {

    fun getHistoriesByMedicineId(medicineId: String) : Flow<List<HistoryDto>>
}