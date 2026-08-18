package com.openclassrooms.rebonnte.domain.useCase.aisle

import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import kotlinx.coroutines.flow.Flow

class GetAislesUseCase(
    private val aisleRepository: AisleRepository,
) {

    operator fun invoke(): Flow<List<Aisle>> {
        return aisleRepository.getAisles()
    }
}