package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.domain.useCase.medicine.GetMedicinesByAisleUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetMedicinesByAisleUseCaseTest {

    private lateinit var medicineRepository: MedicineRepository
    private lateinit var getMedicinesByAisleUseCase: GetMedicinesByAisleUseCase

    @BeforeEach
    fun setUp() {
        medicineRepository = mockk()
        getMedicinesByAisleUseCase = GetMedicinesByAisleUseCase(medicineRepository)
    }

    @Test
    fun `invoke should return flow from repository`() = runTest {
        val aisleNumber = "1"
        val medicines = listOf(
            Medicine(id = "1", name = "Aspirin", aisleNumber = aisleNumber, currentStock = 10)
        )
        val expectedFlow = flowOf(medicines)
        every { medicineRepository.getMedicinesByAisleNumber(aisleNumber) } returns expectedFlow

        val resultFlow = getMedicinesByAisleUseCase(aisleNumber)
        val result = resultFlow.toList()

        assertEquals(listOf(medicines), result)
        verify(exactly = 1) { medicineRepository.getMedicinesByAisleNumber(aisleNumber) }
    }
}
