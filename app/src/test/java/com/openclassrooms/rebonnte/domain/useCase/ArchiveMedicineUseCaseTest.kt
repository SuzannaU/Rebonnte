package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.domain.util.DataResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ArchiveMedicineUseCaseTest {

    private lateinit var medicineRepository: MedicineRepository
    private lateinit var archiveMedicineUseCase: ArchiveMedicineUseCase

    @BeforeEach
    fun setUp() {
        medicineRepository = mockk()
        archiveMedicineUseCase = ArchiveMedicineUseCase(medicineRepository)
    }

    @Test
    fun `invoke should call archiveMedicineById on repository`() = runTest {
        val medicineId = "test_id"
        val expectedResult = DataResult.Success(Unit)
        coEvery { medicineRepository.archiveMedicineById(medicineId) } returns expectedResult

        val result = archiveMedicineUseCase(medicineId)

        assertEquals(expectedResult, result)
        coVerify(exactly = 1) { medicineRepository.archiveMedicineById(match { it == medicineId}) }
    }
}
