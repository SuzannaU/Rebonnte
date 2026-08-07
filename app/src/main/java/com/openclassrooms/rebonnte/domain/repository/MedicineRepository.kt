package com.openclassrooms.rebonnte.domain.repository

import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.model.MedicineSortOption
import com.openclassrooms.rebonnte.domain.model.UpdatedField
import kotlinx.coroutines.flow.Flow

interface MedicineRepository {
    suspend fun getMedicineById(medicineId: String): Medicine?
    fun getMedicinesOrderedBy(sortOption: MedicineSortOption): Flow<List<Medicine>>
    fun getMedicinesByAisleNumber(aisleNumber: String): Flow<List<Medicine>>
    suspend fun addMedicineWithHistory(
        medicine: Medicine,
        history: History
    )

    suspend fun updateMedicineWithHistory(
        medicineId: String,
        updatedField: UpdatedField,
        history: History,
    )

    suspend fun archiveMedicineById(medicineId: String)
}