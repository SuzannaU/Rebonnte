package com.openclassrooms.rebonnte.data.datasource

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import com.openclassrooms.rebonnte.data.dto.HistoryDto
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

    override fun getMedicinesByAisleNumber(aisleNumber: String): Flow<List<MedicineDto>> {
        return firestore
            .collection(MEDICINE_COLLECTION)
            .whereEqualTo("aisleNumber", aisleNumber)
            .orderBy("name", Query.Direction.DESCENDING)
            .dataObjects<MedicineDto>()
    }

    override suspend fun saveMedicineWithHistory(medicine: MedicineDto, history: HistoryDto) {
        println("datasource called")
        firestore.runTransaction { transaction ->
            val medicineDocRef = firestore.collection(MEDICINE_COLLECTION).document(medicine.id)

            transaction.set(medicineDocRef, medicine)

            val historyDocRef = firestore.collection(HISTORY_COLLECTION).document()
            val historyToSave = history.copy(
                id = historyDocRef.id,
                dateTime = Timestamp.now(),
            )

            transaction.set(historyDocRef, historyToSave)
        }.await()
    }

    override suspend fun deleteMedicineById(medicineId: String) {
        firestore.collection(MEDICINE_COLLECTION)
            .document(medicineId)
            .delete()
            .await()
    }
}