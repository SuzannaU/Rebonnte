package com.openclassrooms.rebonnte.data.repositoryImpl

import com.openclassrooms.rebonnte.data.datasource.HistoryDataSource
import com.openclassrooms.rebonnte.data.dto.toDomain
import com.openclassrooms.rebonnte.data.dto.toDto
import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HistoryRepositoryFirestoreImpl(
    private val historyDataSource: HistoryDataSource,
) : HistoryRepository {

    override suspend fun getHistoryById(historyId: String): History? {
        return historyDataSource.getHistoryById(historyId = historyId)?.toDomain()
    }

    override fun getHistories(): Flow<List<History>> {
        return historyDataSource.getHistories().map { historyDtos ->
            historyDtos.map { historyDto ->
                historyDto.toDomain()
            }
        }
    }

    override fun getHistoryByMedicineId(medicineId: String): Flow<List<History>> {
        return historyDataSource.getHistoryByMedicineId(medicineId).map { historyDtos ->
            historyDtos.map { historyDto ->
                historyDto.toDomain()
            }
        }
    }

    override suspend fun saveHistory(history: History) {
        historyDataSource.saveHistory(history.toDto())
    }
}