package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository

class GetMedicineByIdUseCase(
    private val medicineRepository: MedicineRepository,
) {
    suspend operator fun invoke(medicineId: String) : Medicine? {
        return medicineRepository.getMedicineById(medicineId = medicineId)
    }
}