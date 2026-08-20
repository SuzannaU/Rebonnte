package com.openclassrooms.rebonnte.domain.repository

import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.util.DataResult
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {

    // Writing operations for Histories are done by the MedicineRepository because a History only exists tied to its Medicine

    fun getHistoriesByMedicineId(medicineId: String) : Flow<DataResult<List<History>>>
}