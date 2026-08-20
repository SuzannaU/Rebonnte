package com.openclassrooms.rebonnte.data.repositoryImpl

import com.openclassrooms.rebonnte.data.datasource.MedicineDataSource
import com.openclassrooms.rebonnte.data.util.asDataResult
import com.openclassrooms.rebonnte.data.util.toDomain
import com.openclassrooms.rebonnte.data.util.toDto
import com.openclassrooms.rebonnte.data.util.wrapDataResult
import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.model.MedicineSortOption
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.domain.util.DataResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MedicineRepositoryFirestoreImpl(
    private val medicineDataSource: MedicineDataSource,
) : MedicineRepository {

    override fun getMedicineById(medicineId: String): Flow<DataResult<Medicine?>> {
        return medicineDataSource.getMedicineById(medicineId)
            .map { medicine ->
                medicine?.toDomain()
            }
            .asDataResult()
    }

    override fun getMedicinesOrderedBy(sortOption: MedicineSortOption): Flow<DataResult<List<Medicine>>> {
        return medicineDataSource.getUnarchivedMedicinesOrderedBy(sortOption)
            .map { medicineDtos ->
                medicineDtos.map { medicineDto ->
                    medicineDto.toDomain()
                }
            }
            .asDataResult()
    }

    override fun getMedicinesByAisleNumber(aisleNumber: String): Flow<DataResult<List<Medicine>>> {
        return medicineDataSource.getMedicinesByAisleNumber(aisleNumber).map { medicineDtos ->
            medicineDtos.map { medicineDto ->
                medicineDto.toDomain()
            }
        }
            .asDataResult()
    }

    override suspend fun addMedicineWithHistory(
        medicine: Medicine,
        history: History
    ): DataResult<Unit> {
        return wrapDataResult {
            medicineDataSource.addMedicineWithHistory(medicine.toDto(), history.toDto())
        }
    }

    override suspend fun updateMedicineWithHistory(
        medicineId: String,
        history: History,
    ): DataResult<Unit> {
        return wrapDataResult {
            medicineDataSource.updateMedicineWithHistory(
                medicineId = medicineId,
                history = history.toDto(),
            )
        }
    }

    override suspend fun archiveMedicineWithHistory(
        medicineId: String,
        history: History
    ): DataResult<Unit> {
        return wrapDataResult {
            medicineDataSource.archiveMedicineWithHistory(
                medicineId = medicineId,
                history = history.toDto()
            )
        }
    }
}