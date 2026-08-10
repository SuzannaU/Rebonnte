package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.repository.MedicineRepository

class ArchiveMedicineUseCase(
    private val medicineRepository: MedicineRepository,
) {

    suspend operator fun invoke(medicineId: String) {
        medicineRepository.archiveMedicineById(medicineId)
    }
}