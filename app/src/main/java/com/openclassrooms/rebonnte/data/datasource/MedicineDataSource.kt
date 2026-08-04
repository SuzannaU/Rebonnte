package com.openclassrooms.rebonnte.data.datasource

import com.openclassrooms.rebonnte.data.dto.HistoryDto
import com.openclassrooms.rebonnte.data.dto.MedicineDto
import kotlinx.coroutines.flow.Flow

interface MedicineDataSource {
    suspend fun getMedicineById(medicineId: String): MedicineDto?
    fun getMedicines(): Flow<List<MedicineDto>>
    fun getMedicinesByAisleNumber(aisleNumber: String): Flow<List<MedicineDto>>

    suspend fun saveMedicineWithHistory(medicine: MedicineDto, history: HistoryDto)
    suspend fun deleteMedicineById(medicineId: String)
}