package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.exception.DataValidationException
import com.openclassrooms.rebonnte.domain.model.AuthUser
import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.model.UpdatedField
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.domain.service.AuthService
import com.openclassrooms.rebonnte.domain.util.DataResult
import com.openclassrooms.rebonnte.domain.util.wrapDataResult
import java.util.Calendar

class UpdateMedicineUseCase(
    private val medicineRepository: MedicineRepository,
    private val authService: AuthService,
) {

    suspend operator fun invoke(medicineId: String, updatedField: UpdatedField): DataResult<Unit> {

        val userResult = wrapDataResult {
            authService.getAuthUser()
        }
        when (userResult) {
            is DataResult.Failure -> return userResult
            is DataResult.Success<AuthUser> -> {
                val user = userResult.data

                val history = History(
                    medicineId = medicineId,
                    userId = user.uid,
                    dateTime = Calendar.getInstance().time,
                    updatedField = updatedField,
                )

                return medicineRepository.updateMedicineWithHistory(
                    medicineId = medicineId,
                    history = history
                )
            }
        }
    }
}

