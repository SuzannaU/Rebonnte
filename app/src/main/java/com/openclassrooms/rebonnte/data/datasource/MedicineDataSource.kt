package com.openclassrooms.rebonnte.data.datasource

import com.openclassrooms.rebonnte.data.dto.HistoryDto
import com.openclassrooms.rebonnte.data.dto.MedicineDto
import com.openclassrooms.rebonnte.data.dto.UpdatedFieldDto
import com.openclassrooms.rebonnte.domain.model.MedicineSortOption
import kotlinx.coroutines.flow.Flow

interface MedicineDataSource {
    suspend fun getMedicineById(medicineId: String): MedicineDto?
    fun getUnarchivedMedicinesOrderedBy(sortOption: MedicineSortOption): Flow<List<MedicineDto>>
    fun getMedicinesByAisleNumber(aisleNumber: String): Flow<List<MedicineDto>>
    suspend fun addMedicineWithHistory(medicine: MedicineDto, history: HistoryDto)
    suspend fun updateMedicineWithHistory(
        medicineId: String,
        history: HistoryDto
    )
    suspend fun archiveMedicineById(medicineId: String)
}