package com.openclassrooms.rebonnte.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import com.openclassrooms.rebonnte.data.dto.HistoryDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

const val HISTORY_COLLECTION = "histories"
private const val DATETIME_FIELD = "dateTime"
private const val MEDICINE_ID_FIELD = "medicineId"

class HistoryFirestoreDataSource(
    private val firestore: FirebaseFirestore,
) : HistoryDataSource {

    override suspend fun getHistoryById(historyId: String): HistoryDto? {
        return firestore
            .collection(HISTORY_COLLECTION)
            .document(historyId)
            .get()
            .await()
            .toObject<HistoryDto>()
    }

    override fun getHistories(): Flow<List<HistoryDto>> {
        return firestore
            .collection(HISTORY_COLLECTION)
            .orderBy(DATETIME_FIELD, Query.Direction.DESCENDING)
            .dataObjects<HistoryDto>()
    }

    override fun getHistoryByMedicineId(medicineId: String): Flow<List<HistoryDto>> {
        return firestore
            .collection(HISTORY_COLLECTION)
            .whereEqualTo(MEDICINE_ID_FIELD, medicineId)
            .orderBy(DATETIME_FIELD, Query.Direction.DESCENDING)
            .dataObjects<HistoryDto>()
    }

    override suspend fun saveHistory(history: HistoryDto) {
        val docRef = if (history.id.isEmpty()) {
            firestore.collection(HISTORY_COLLECTION).document()
        } else {
            firestore.collection(HISTORY_COLLECTION).document(history.id)
        }

        val historyToSave = if (history.id.isEmpty()) {
            history.copy(id = docRef.id)
        } else {
            history
        }

        docRef.set(historyToSave).await()
    }
}