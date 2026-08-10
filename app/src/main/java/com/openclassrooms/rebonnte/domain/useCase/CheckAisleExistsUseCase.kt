package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.repository.AisleRepository

class CheckAisleExistsUseCase(
    private val aisleRepository: AisleRepository
) {
    suspend operator fun invoke(aisleNumber: String): Boolean {
        return aisleRepository.getAisleByNumber(aisleNumber) != null
    }
}