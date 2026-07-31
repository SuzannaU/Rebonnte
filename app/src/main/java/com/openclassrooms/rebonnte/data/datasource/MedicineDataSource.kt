package com.openclassrooms.rebonnte.data.datasource

import com.openclassrooms.rebonnte.data.dto.MedicineDto
import kotlinx.coroutines.flow.Flow

interface MedicineDataSource {
    suspend fun getMedicineById(medicineId: String) : MedicineDto?
    fun getMedicines() : Flow<List<MedicineDto>>
    fun getMedicinesByAisleId(aisleId: String) : Flow<List<MedicineDto>>

    suspend fun saveMedicine(medicine: MedicineDto)
    suspend fun deleteMedicineById(medicineId: String)
}