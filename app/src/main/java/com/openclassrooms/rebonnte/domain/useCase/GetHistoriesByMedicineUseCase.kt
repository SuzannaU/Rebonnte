package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow

class GetHistoriesByMedicineUseCase(
    private val historyRepository: HistoryRepository,
) {

    operator fun invoke(medicineId: String): Flow<List<History>> {
        return historyRepository.getHistoriesByMedicineId(medicineId = medicineId)
    }
}