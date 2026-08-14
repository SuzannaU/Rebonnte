package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import com.openclassrooms.rebonnte.domain.util.DataResult

class AddAisleUseCase(
    private val aisleRepository: AisleRepository,
) {
    suspend operator fun invoke(aisleNumber: String): DataResult<Unit> {
        return aisleRepository.addAisle(
            Aisle(
                number = aisleNumber
            )
        )
    }
}