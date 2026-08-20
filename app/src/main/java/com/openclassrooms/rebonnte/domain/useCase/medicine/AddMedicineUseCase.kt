package com.openclassrooms.rebonnte.domain.useCase.medicine

import com.openclassrooms.rebonnte.domain.model.AuthUser
import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.domain.service.AuthService
import com.openclassrooms.rebonnte.domain.util.DataResult
import java.util.Calendar

class AddMedicineUseCase(
    private val medicineRepository: MedicineRepository,
    private val authService: AuthService,
) {

    suspend operator fun invoke(medicine: Medicine): DataResult<Unit> {

        when (val userResult = authService.getAuthUser()) {
            is DataResult.Failure -> return userResult
            is DataResult.Success<AuthUser> -> {
                val user = userResult.data

                val history = History(
                    medicineId = medicine.id,
                    userId = user.uid,
                    dateTime = Calendar.getInstance().time,
                    isCreation = true,
                )

                return medicineRepository.addMedicineWithHistory(medicine, history)
            }
        }
    }
}