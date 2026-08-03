package com.openclassrooms.rebonnte.data.repositoryImpl

import com.openclassrooms.rebonnte.data.datasource.MedicineDataSource
import com.openclassrooms.rebonnte.data.dto.toDomain
import com.openclassrooms.rebonnte.data.dto.toDto
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MedicineRepositoryFirestoreImpl(
    private val medicineDataSource: MedicineDataSource,
) : MedicineRepository {
    override suspend fun getMedicineById(medicineId: String): Medicine? {
        return medicineDataSource.getMedicineById(medicineId)?.toDomain()
    }

    override fun getMedicines(): Flow<List<Medicine>> {
        return medicineDataSource.getMedicines().map { medicineDtos ->
            medicineDtos.map { medicineDto ->
                medicineDto.toDomain()
            }
        }
    }

    override fun getMedicinesByAisleId(aisleId: String): Flow<List<Medicine>> {
        return medicineDataSource.getMedicinesByAisleId(aisleId).map { medicineDtos ->
            medicineDtos.map { medicineDto ->
                medicineDto.toDomain()
            }
        }
    }

    override suspend fun addMedicine(medicine: Medicine) : String {
        return medicineDataSource.saveMedicine(medicine.toDto())
    }

    override suspend fun deleteMedicineById(medicineId: String) {
        medicineDataSource.deleteMedicineById(medicineId)
    }
}