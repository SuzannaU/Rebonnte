package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.model.AuthUser
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.domain.service.AuthService
import com.openclassrooms.rebonnte.domain.useCase.medicine.AddMedicineUseCase
import com.openclassrooms.rebonnte.domain.util.DataResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddMedicineUseCaseTest {

    private lateinit var medicineRepository: MedicineRepository
    private lateinit var authService: AuthService
    private lateinit var addMedicineUseCase: AddMedicineUseCase

    @BeforeEach
    fun setUp() {
        medicineRepository = mockk()
        authService = mockk()
        addMedicineUseCase = AddMedicineUseCase(medicineRepository, authService)
    }

    @Test
    fun `invoke should return failure when authService fails`() = runTest {
        val medicine = Medicine(id = "1", name = "Aspirin", aisleNumber = "1", currentStock = 10)
        coEvery { authService.getAuthUser() } returns DataResult.Failure(Exception())

        val result = addMedicineUseCase(medicine)

        assertTrue(result is DataResult.Failure)
        coVerify(exactly = 0) {
            medicineRepository.addMedicineWithHistory(any(), any())
        }
    }

    @Test
    fun `invoke should call addMedicineWithHistory when authService succeeds`() = runTest {
        val medicine = Medicine(id = "1", name = "Aspirin", aisleNumber = "1", currentStock = 10)
        val authUser = AuthUser(uid = "user1", email = "test@test.com", displayName = "Tester")
        coEvery { authService.getAuthUser() } returns DataResult.Success(authUser)

        val expectedResult = DataResult.Success(Unit)
        coEvery { medicineRepository.addMedicineWithHistory(any(), any()) } returns expectedResult

        val result = addMedicineUseCase(medicine)

        assertEquals(expectedResult, result)
        coVerify(exactly = 1) {
            medicineRepository.addMedicineWithHistory(
                medicine = medicine,
                history = match {
                    it.medicineId == medicine.id &&
                            it.userId == authUser.uid &&
                            it.isCreation
                }
            )
        }
    }
}
