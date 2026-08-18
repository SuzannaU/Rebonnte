package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.domain.useCase.medicine.GetMedicineByIdUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetMedicineByIdUseCaseTest {

    private lateinit var medicineRepository: MedicineRepository
    private lateinit var getMedicineByIdUseCase: GetMedicineByIdUseCase

    @BeforeEach
    fun setUp() {
        medicineRepository = mockk()
        getMedicineByIdUseCase = GetMedicineByIdUseCase(medicineRepository)
    }

    @Test
    fun `invoke should return flow from repository`() = runTest {
        val medicineId = "med_id"
        val medicine = Medicine(id = medicineId, name = "Aspirin", aisleNumber = "1", currentStock = 10)
        val expectedFlow = flowOf(medicine)
        every { medicineRepository.getMedicineById(medicineId) } returns expectedFlow

        val resultFlow = getMedicineByIdUseCase(medicineId)
        val result = resultFlow.toList()

        assertEquals(listOf(medicine), result)
        verify(exactly = 1) { medicineRepository.getMedicineById(medicineId) }
    }
}
