package com.openclassrooms.rebonnte.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import com.openclassrooms.rebonnte.data.dto.AisleDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

private const val AISlE_COLLECTION = "aisles"

class AisleFirestoreDataSource(
    private val firestore: FirebaseFirestore,
) : AisleDataSource {

    override suspend fun getAisleById(id: String): AisleDto? {
        return firestore
            .collection(AISlE_COLLECTION)
            .document(id)
            .get()
            .await()
            .toObject<AisleDto>()
    }

    override suspend fun getAisleByNumber(number: Int): AisleDto? {
        return firestore
            .collection(AISlE_COLLECTION)
            .whereEqualTo("number", number)
            .dataObjects<AisleDto>()
            .first()[0]
    }

    override fun getAisles(): Flow<List<AisleDto>> {
        return firestore
            .collection(AISlE_COLLECTION)
            .dataObjects<AisleDto>()
    }

    override suspend fun saveAisle(aisle: AisleDto) {
        val docRef = if (aisle.id.isEmpty()) {
            firestore.collection(AISlE_COLLECTION).document()
        } else {
            firestore.collection(AISlE_COLLECTION).document(aisle.id)
        }

        val aisleToSave = if (aisle.id.isEmpty()) {
            aisle.copy(id = docRef.id)
        } else {
            aisle
        }

        docRef.set(aisleToSave).await()
    }

}