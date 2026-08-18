package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.model.MedicineSortOption
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetMedicinesOrderedByUseCaseTest {

    private lateinit var medicineRepository: MedicineRepository
    private lateinit var getMedicinesOrderedByUseCase: GetMedicinesOrderedByUseCase

    @BeforeEach
    fun setUp() {
        medicineRepository = mockk()
        getMedicinesOrderedByUseCase = GetMedicinesOrderedByUseCase(medicineRepository)
    }

    @Test
    fun `invoke should return flow from repository`() = runTest {
        val sortOption = MedicineSortOption.NAME_ASCENDING
        val medicines = listOf(
            Medicine(id = "1", name = "Aspirin", aisleNumber = "1", currentStock = 10)
        )
        val expectedFlow = flowOf(medicines)
        every { medicineRepository.getMedicinesOrderedBy(sortOption) } returns expectedFlow

        val resultFlow = getMedicinesOrderedByUseCase(sortOption)
        val result = resultFlow.toList()

        assertEquals(listOf(medicines), result)
        verify(exactly = 1) { medicineRepository.getMedicinesOrderedBy(sortOption) }
    }
}
