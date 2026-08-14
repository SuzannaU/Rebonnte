package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.domain.util.DataResult

class GetMedicineByIdUseCase(
    private val medicineRepository: MedicineRepository,
) {

    suspend operator fun invoke(medicineId: String) : DataResult<Medicine?> {
        return medicineRepository.getMedicineById(medicineId = medicineId)
    }
}