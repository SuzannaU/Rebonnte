package com.openclassrooms.rebonnte.domain.useCase.aisle

import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import com.openclassrooms.rebonnte.domain.util.DataResult

class CheckAisleExistsUseCase(
    private val aisleRepository: AisleRepository
) {

    suspend operator fun invoke(aisleNumber: String): DataResult<Boolean> {
        return when (val result = aisleRepository.getAisleByNumber(aisleNumber)) {
            is DataResult.Success -> DataResult.Success(result.data != null)
            is DataResult.Failure -> DataResult.Failure(result.exception)
        }
    }
}