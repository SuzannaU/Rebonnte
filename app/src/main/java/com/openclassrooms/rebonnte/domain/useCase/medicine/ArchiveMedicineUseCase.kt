package com.openclassrooms.rebonnte.domain.useCase.medicine

import com.openclassrooms.rebonnte.domain.model.AuthUser
import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.domain.service.AuthService
import com.openclassrooms.rebonnte.domain.util.DataResult
import java.util.Calendar

class ArchiveMedicineUseCase(
    private val medicineRepository: MedicineRepository,
    private val authService: AuthService,
) {

    suspend operator fun invoke(medicineId: String): DataResult<Unit> {

        when (val userResult = authService.getAuthUser()) {
            is DataResult.Failure -> return userResult
            is DataResult.Success<AuthUser> -> {
                val user = userResult.data

                val history = History(
                    medicineId = medicineId,
                    userId = user.uid,
                    dateTime = Calendar.getInstance().time,
                    isArchiving = true,
                )

                return medicineRepository.archiveMedicineWithHistory(
                    medicineId = medicineId,
                    history = history
                )
            }
        }
    }
}