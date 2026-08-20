package com.openclassrooms.rebonnte.domain.repository

import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.model.MedicineSortOption
import com.openclassrooms.rebonnte.domain.util.DataResult
import kotlinx.coroutines.flow.Flow

interface MedicineRepository {
    fun getMedicineById(medicineId: String): Flow<DataResult<Medicine?>>
    fun getMedicinesOrderedBy(sortOption: MedicineSortOption): Flow<DataResult<List<Medicine>>>
    fun getMedicinesByAisleNumber(aisleNumber: String): Flow<DataResult<List<Medicine>>>
    suspend fun addMedicineWithHistory(
        medicine: Medicine,
        history: History
    ): DataResult<Unit>

    suspend fun updateMedicineWithHistory(
        medicineId: String,
        history: History,
    ): DataResult<Unit>

    suspend fun archiveMedicineWithHistory(
        medicineId: String,
        history: History
    ): DataResult<Unit>
}