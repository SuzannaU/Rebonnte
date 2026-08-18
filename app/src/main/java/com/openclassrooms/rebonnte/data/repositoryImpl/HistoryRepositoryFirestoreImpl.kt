package com.openclassrooms.rebonnte.data.repositoryImpl

import com.openclassrooms.rebonnte.data.datasource.HistoryDataSource
import com.openclassrooms.rebonnte.data.dto.toDomain
import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HistoryRepositoryFirestoreImpl(
    private val historyDataSource: HistoryDataSource,
) : HistoryRepository {

    // Writing operations for Histories are done by the MedicineRepository because a History writing is tied to its Medicine

    override fun getHistoriesByMedicineId(medicineId: String): Flow<List<History>> {
        return historyDataSource.getHistoriesByMedicineId(medicineId).map { historyDtos ->
            historyDtos.map { historyDto ->
                historyDto.toDomain()
            }
        }
    }
}