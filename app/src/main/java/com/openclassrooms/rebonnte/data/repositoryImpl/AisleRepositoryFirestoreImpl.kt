package com.openclassrooms.rebonnte.data.repositoryImpl

import com.openclassrooms.rebonnte.data.datasource.AisleDataSource
import com.openclassrooms.rebonnte.data.dto.toDomain
import com.openclassrooms.rebonnte.data.dto.toDto
import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import com.openclassrooms.rebonnte.domain.util.DataResult
import com.openclassrooms.rebonnte.domain.util.wrapDataResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AisleRepositoryFirestoreImpl(
    private val aisleDataSource: AisleDataSource,
) : AisleRepository {

    override suspend fun getAisleByNumber(aisleNumber: String): DataResult<Aisle?> {
        return wrapDataResult {
            aisleDataSource.getAisleByNumber(aisleNumber)?.toDomain()
        }
    }

    override fun getAisles(): Flow<List<Aisle>> {
        return aisleDataSource.getAisles().map { aisleDtos ->
            aisleDtos.map { aisleDto ->
                aisleDto.toDomain()
            }
        }
    }

    override suspend fun addAisle(aisle: Aisle): DataResult<Unit> {
        return wrapDataResult {
            aisleDataSource.saveAisle(aisle.toDto())
        }
    }
}