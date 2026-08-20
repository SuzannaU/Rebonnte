package com.openclassrooms.rebonnte.domain.useCase.medicine

import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.repository.HistoryRepository
import com.openclassrooms.rebonnte.domain.util.DataResult
import kotlinx.coroutines.flow.Flow

class GetHistoriesByMedicineUseCase(
    private val historyRepository: HistoryRepository,
) {

    operator fun invoke(medicineId: String): Flow<DataResult<List<History>>> {
        return historyRepository.getHistoriesByMedicineId(medicineId = medicineId)
    }
}