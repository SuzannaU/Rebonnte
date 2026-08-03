package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.model.User
import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import com.openclassrooms.rebonnte.domain.repository.HistoryRepository
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.domain.repository.UserRepository
import java.util.Calendar

class AddMedicineUseCase(
    private val medicineRepository: MedicineRepository,
    private val historyRepository: HistoryRepository,
    private val aisleRepository: AisleRepository,
    private val userRepository: UserRepository,
    ) {

    suspend fun execute(
        aisleNumber: Int,
        medicine: Medicine
    ) {

        val aisleId =
            aisleRepository.getAisleByNumber(aisleNumber)?.id ?: "No Aisle ID"

        val medicineToSave = medicine.copy(aisleId = aisleId)

        val medicineId = medicineRepository.addMedicine(medicineToSave)

//        val user = userRepository.getCurrentUser() ?: User("","","")
//        val historyToSave= History(
//            medicineId = medicineId,
//            userId = "userId",
//            dateTime = Calendar.getInstance().time,
//            details = "${medicine.name} created by ${user.username}"
//        )
//
//        historyRepository.saveHistory(historyToSave)

    }
}