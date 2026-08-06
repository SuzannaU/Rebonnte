package com.openclassrooms.rebonnte.data.datasource

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import com.openclassrooms.rebonnte.data.dto.HistoryDto
import com.openclassrooms.rebonnte.data.dto.MedicineDto
import com.openclassrooms.rebonnte.data.dto.UpdatedFieldDto
import com.openclassrooms.rebonnte.domain.model.UpdatableFields
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

private const val MEDICINE_COLLECTION = "medicines"
private const val NAME_FIELD = "name"
private const val AISLE_NUMBER_FIELD = "aisleNumber"
private const val CURRENT_STOCK_FIELD = "currentStock"
private const val ARCHIVED_FIELD = "archived"

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
            .orderBy(NAME_FIELD, Query.Direction.DESCENDING)
            .dataObjects<MedicineDto>()
    }

    override fun getUnarchivedMedicines(): Flow<List<MedicineDto>> {
        return firestore
            .collection(MEDICINE_COLLECTION)
            .whereEqualTo(ARCHIVED_FIELD, false)
            .orderBy(NAME_FIELD, Query.Direction.DESCENDING)
            .dataObjects<MedicineDto>()
    }

    override fun getMedicinesByAisleNumber(aisleNumber: String): Flow<List<MedicineDto>> {
        return firestore
            .collection(MEDICINE_COLLECTION)
            .whereEqualTo(ARCHIVED_FIELD, false)
            .whereEqualTo(AISLE_NUMBER_FIELD, aisleNumber)
            .orderBy(NAME_FIELD, Query.Direction.DESCENDING)
            .dataObjects<MedicineDto>()
    }

    override suspend fun addMedicineWithHistory(medicine: MedicineDto, history: HistoryDto) {
        val medicineDocRef = firestore.collection(MEDICINE_COLLECTION).document(medicine.id)

        firestore.runTransaction { transaction ->

            val historyDocRef = firestore.collection(HISTORY_COLLECTION)
                .document()         //Test with predefined doc id
            val historyDocument = transaction.get(historyDocRef)
            if (historyDocument.exists()) {
                throw FirebaseFirestoreException(
                    "History doc ref already exists",
                    FirebaseFirestoreException.Code.ALREADY_EXISTS
                )
            }
            val historyToSave = history.copy(
                id = historyDocRef.id,
                dateTime = Timestamp.now(),
            )

            transaction.set(medicineDocRef, medicine)
            transaction.set(historyDocRef, historyToSave)
        }.await()
    }

    override suspend fun updateMedicineWithHistory(
        medicineId: String,
        updatedField: UpdatedFieldDto,
        history: HistoryDto,
    ) {
        val medicineDocRef = firestore.collection(MEDICINE_COLLECTION).document(medicineId)

        val updatedData = hashMapOf<String, Any>()
        when {
            (updatedField.field == UpdatableFields.NAME) -> {
                updatedData[NAME_FIELD] = updatedField.newValue
            }

            (updatedField.field == UpdatableFields.AISLE) -> {
                updatedData[AISLE_NUMBER_FIELD] = updatedField.newValue
            }

            (updatedField.field == UpdatableFields.STOCK) -> {
                updatedData[CURRENT_STOCK_FIELD] = updatedField.newValue.toInt()
            }
        }

        firestore.runTransaction { transaction ->
            val historyDocRef = firestore.collection(HISTORY_COLLECTION)
                .document()         //Test with predefined doc id
            val historyDocument = transaction.get(historyDocRef)
            if (historyDocument.exists()) {
                throw FirebaseFirestoreException(
                    "History doc ref already exists",
                    FirebaseFirestoreException.Code.ALREADY_EXISTS
                )
            }
            val historyToSave = history.copy(
                id = historyDocRef.id,
                dateTime = Timestamp.now()
            )

            transaction.update(medicineDocRef, updatedData)
            transaction.set(historyDocRef, historyToSave)
        }.await()
    }

    override suspend fun archiveMedicineById(medicineId: String) {
        val docRef = firestore.collection(MEDICINE_COLLECTION)
            .document(medicineId)
        docRef.update(ARCHIVED_FIELD, true)
    }
}