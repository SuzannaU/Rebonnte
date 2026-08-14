package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import kotlinx.coroutines.flow.Flow

class GetMedicinesByAisleUseCase(
    private val medicineRepository: MedicineRepository,
) {

    operator fun invoke(aisleNumber: String): Flow<List<Medicine>> {
        return medicineRepository.getMedicinesByAisleNumber(aisleNumber)
    }
}