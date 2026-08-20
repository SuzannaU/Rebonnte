package com.openclassrooms.rebonnte.domain.useCase.medicine

import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.domain.util.DataResult
import kotlinx.coroutines.flow.Flow

class GetMedicinesByAisleUseCase(
    private val medicineRepository: MedicineRepository,
) {

    operator fun invoke(aisleNumber: String): Flow<DataResult<List<Medicine>>> {
        return medicineRepository.getMedicinesByAisleNumber(aisleNumber)
    }
}