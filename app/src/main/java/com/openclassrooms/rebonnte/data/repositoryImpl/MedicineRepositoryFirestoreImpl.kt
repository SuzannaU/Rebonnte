package com.openclassrooms.rebonnte.data.repositoryImpl

import com.openclassrooms.rebonnte.data.datasource.MedicineDataSource
import com.openclassrooms.rebonnte.data.dto.toDomain
import com.openclassrooms.rebonnte.data.dto.toDto
import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.model.UpdatedField
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

    override fun getMedicinesByAisleNumber(aisleNumber: String): Flow<List<Medicine>> {
        return medicineDataSource.getMedicinesByAisleNumber(aisleNumber).map { medicineDtos ->
            medicineDtos.map { medicineDto ->
                medicineDto.toDomain()
            }
        }
    }

    override suspend fun addMedicineWithHistory(medicine: Medicine, history: History) {
        medicineDataSource.addMedicineWithHistory(medicine.toDto(), history.toDto())
    }

    override suspend fun updateMedicineWithHistory(
        medicineId: String,
        updatedField: UpdatedField,
        history: History,
    ) {
        medicineDataSource.updateMedicineWithHistory(
            medicineId = medicineId,
            updatedField = updatedField.toDto(),
            history = history.toDto(),
        )
    }

    override suspend fun deleteMedicineById(medicineId: String) {
        medicineDataSource.deleteMedicineById(medicineId)
    }
}