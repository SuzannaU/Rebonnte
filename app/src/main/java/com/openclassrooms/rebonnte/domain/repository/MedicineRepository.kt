package com.openclassrooms.rebonnte.domain.repository

import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.model.MedicineSortOption
import com.openclassrooms.rebonnte.domain.model.UpdatedField
import com.openclassrooms.rebonnte.domain.util.DataResult
import kotlinx.coroutines.flow.Flow

interface MedicineRepository {
    suspend fun getMedicineById(medicineId: String): DataResult<Medicine?>
    fun getMedicinesOrderedBy(sortOption: MedicineSortOption): Flow<List<Medicine>>
    fun getMedicinesByAisleNumber(aisleNumber: String): Flow<List<Medicine>>
    suspend fun addMedicineWithHistory(
        medicine: Medicine,
        history: History
    ): DataResult<Unit>

    suspend fun updateMedicineWithHistory(
        medicineId: String,
        updatedField: UpdatedField,
        history: History,
    ): DataResult<Unit>

    suspend fun archiveMedicineById(medicineId: String): DataResult<Unit>
}