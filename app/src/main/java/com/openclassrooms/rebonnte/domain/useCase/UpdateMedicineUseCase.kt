package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.model.UpdatedField
import com.openclassrooms.rebonnte.domain.model.User
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.domain.repository.UserRepository
import java.util.Calendar

class UpdateMedicineUseCase(
    private val medicineRepository: MedicineRepository,
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(medicineId: String, updatedField: UpdatedField) {
        val user = userRepository.getCurrentUser() ?: User("", "", "")

        val history = History(
            medicineId = medicineId,
            userId = user.id,
            dateTime = Calendar.getInstance().time,
            updatedField = updatedField,
        )

        medicineRepository.updateMedicineWithHistory(
            medicineId = medicineId,
            updatedField = updatedField,
            history = history
        )
    }
}

