package com.openclassrooms.rebonnte.data.repositoryImpl

import com.openclassrooms.rebonnte.data.datasource.MedicineDataSource
import com.openclassrooms.rebonnte.data.dto.toDomain
import com.openclassrooms.rebonnte.data.dto.toDto
import com.openclassrooms.rebonnte.data.util.toDomainException
import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.model.MedicineSortOption
import com.openclassrooms.rebonnte.domain.model.UpdatedField
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.domain.util.DataResult
import com.openclassrooms.rebonnte.domain.util.wrapDataResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MedicineRepositoryFirestoreImpl(
    private val medicineDataSource: MedicineDataSource,
) : MedicineRepository {
    override suspend fun getMedicineById(medicineId: String): DataResult<Medicine?> {
        return wrapDataResult(onError = { it.toDomainException() }) {
            medicineDataSource.getMedicineById(medicineId)?.toDomain()
        }
    }

    override fun getMedicinesOrderedBy(sortOption: MedicineSortOption): Flow<List<Medicine>> {
        return medicineDataSource.getUnarchivedMedicinesOrderedBy(sortOption).map { medicineDtos ->
            medicineDtos.map { medicineDto ->
                medicineDto.toDomain()
            }
        }
    }

    override fun getMedicinesByAisleNumber(aisleNumber: String): Flow<List<Medicine>> {
        return medicineDataSource.getMedicinesByAisleNumber(aisleNumber).map { medicineDtos ->
            medicineDtos.map { medicineDto ->
                medicineDto.toDomain()
            }
        }
    }

    override suspend fun addMedicineWithHistory(medicine: Medicine, history: History): DataResult<Unit> {
        return wrapDataResult(onError = { it.toDomainException() }) {
            medicineDataSource.addMedicineWithHistory(medicine.toDto(), history.toDto())
        }
    }

    override suspend fun updateMedicineWithHistory(
        medicineId: String,
        updatedField: UpdatedField,
        history: History,
    ): DataResult<Unit> {
        return wrapDataResult(onError = { it.toDomainException() }) {
            medicineDataSource.updateMedicineWithHistory(
                medicineId = medicineId,
                updatedField = updatedField.toDto(),
                history = history.toDto(),
            )
        }
    }

    override suspend fun archiveMedicineById(medicineId: String): DataResult<Unit> {
        return wrapDataResult(onError = { it.toDomainException() }) {
            medicineDataSource.archiveMedicineById(medicineId)
        }
    }
}