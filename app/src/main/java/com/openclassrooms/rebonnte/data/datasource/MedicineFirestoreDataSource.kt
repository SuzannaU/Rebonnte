package com.openclassrooms.rebonnte.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import com.openclassrooms.rebonnte.data.dto.MedicineDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

private const val MEDICINE_COLLECTION = "medicines"

class MedicineFirestoreDataSource(
    private val firestore: FirebaseFirestore,
) : MedicineDataSource {

    override suspend fun getMedicineById(medicineId: String): MedicineDto? {
        return firestore
            .collection(MEDICINE_COLLECTION)
            .document(medicineId)
            .get()
            .await()
            .toObject<MedicineDto>()
    }

    override fun getMedicines(): Flow<List<MedicineDto>> {
        return firestore
            .collection(MEDICINE_COLLECTION)
            .orderBy("name", Query.Direction.DESCENDING)
            .dataObjects<MedicineDto>()
    }

    override fun getMedicinesByAisleId(aisleId: String): Flow<List<MedicineDto>> {
        return firestore
            .collection(MEDICINE_COLLECTION)
            .whereEqualTo("aisle_id", aisleId)
            .orderBy("name", Query.Direction.DESCENDING)
            .dataObjects<MedicineDto>()
    }

    override suspend fun saveMedicine(medicine: MedicineDto) {
        val docRef = if (medicine.id.isEmpty()) {
            firestore.collection(MEDICINE_COLLECTION).document()
        } else {
            firestore.collection(MEDICINE_COLLECTION).document(medicine.id)
        }

        val medicineToSave = if (medicine.id.isEmpty()) {
            medicine.copy(id = docRef.id)
        } else {
            medicine
        }

        docRef.set(medicineToSave).await()
    }

    override suspend fun deleteMedicineById(medicineId: String) {
        firestore.collection(MEDICINE_COLLECTION)
            .document(medicineId)
            .delete()
            .await()
    }
}