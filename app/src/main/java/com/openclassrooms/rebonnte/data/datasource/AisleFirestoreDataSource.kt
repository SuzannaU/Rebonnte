package com.openclassrooms.rebonnte.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import com.openclassrooms.rebonnte.data.dto.AisleDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

private const val AISlE_COLLECTION = "aisles"

class AisleFirestoreDataSource(
    private val firestore: FirebaseFirestore,
) : AisleDataSource {

    override suspend fun getAisleByNumber(number: String): AisleDto? {
        return firestore
            .collection(AISlE_COLLECTION)
            .document(number)
            .get()
            .await()
            .toObject<AisleDto>()
    }

    override fun getAisles(): Flow<List<AisleDto>> {
        return firestore
            .collection(AISlE_COLLECTION)
            .dataObjects<AisleDto>()
    }

    override suspend fun saveAisle(aisle: AisleDto) {
        val docRef = firestore.collection(AISlE_COLLECTION).document(aisle.id)
        docRef.set(aisle).await()
    }

}