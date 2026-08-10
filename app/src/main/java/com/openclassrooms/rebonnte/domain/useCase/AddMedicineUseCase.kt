package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.model.User
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.domain.repository.UserRepository
import java.util.Calendar

class AddMedicineUseCase(
    private val medicineRepository: MedicineRepository,
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(
        medicine: Medicine
    ) {
        val user = userRepository.getCurrentUser() ?: User("", "", "")
        val history = History(
            medicineId = medicine.id,
            userId = user.id,
            dateTime = Calendar.getInstance().time,
            isCreation = true,
        )

        medicineRepository.addMedicineWithHistory(medicine, history)
    }
}