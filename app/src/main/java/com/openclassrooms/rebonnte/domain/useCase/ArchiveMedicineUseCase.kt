package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.domain.util.DataResult

class ArchiveMedicineUseCase(
    private val medicineRepository: MedicineRepository,
) {

    suspend operator fun invoke(medicineId: String): DataResult<Unit> {
        return medicineRepository.archiveMedicineById(medicineId)
    }
}