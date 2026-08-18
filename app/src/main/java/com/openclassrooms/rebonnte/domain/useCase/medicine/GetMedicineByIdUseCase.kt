package com.openclassrooms.rebonnte.domain.useCase.medicine

import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import kotlinx.coroutines.flow.Flow

class GetMedicineByIdUseCase(
    private val medicineRepository: MedicineRepository,
) {

    operator fun invoke(medicineId: String) : Flow<Medicine?> {
        return medicineRepository.getMedicineById(medicineId = medicineId)
    }
}