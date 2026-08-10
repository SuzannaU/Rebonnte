package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.repository.AisleRepository

class AddAisleUseCase(
    private val aisleRepository: AisleRepository,
) {
    suspend operator fun invoke(aisleNumber: String) {
        aisleRepository.addAisle(Aisle(number = aisleNumber))
    }
}