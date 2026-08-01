package com.openclassrooms.rebonnte.domain.repository

import com.openclassrooms.rebonnte.data.dto.MedicineDto
import com.openclassrooms.rebonnte.domain.model.Medicine
import kotlinx.coroutines.flow.Flow

interface MedicineRepository {
    suspend fun getMedicineById(medicineId: String) : Medicine?
    fun getMedicines() : Flow<List<Medicine>>
    fun getMedicinesByAisleId(aisleId: String) : Flow<List<Medicine>>
    suspend fun addMedicine(medicine: Medicine)
    suspend fun deleteMedicineById(medicineId: String)
}