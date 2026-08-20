package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.model.AuthUser
import com.openclassrooms.rebonnte.domain.model.UpdatableFields
import com.openclassrooms.rebonnte.domain.model.UpdatedField
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.domain.service.AuthService
import com.openclassrooms.rebonnte.domain.useCase.medicine.UpdateMedicineUseCase
import com.openclassrooms.rebonnte.domain.util.DataResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UpdateMedicineUseCaseTest {

    private lateinit var medicineRepository: MedicineRepository
    private lateinit var authService: AuthService
    private lateinit var updateMedicineUseCase: UpdateMedicineUseCase

    @BeforeEach
    fun setUp() {
        medicineRepository = mockk()
        authService = mockk()
        updateMedicineUseCase = UpdateMedicineUseCase(medicineRepository, authService)
    }

    @Test
    fun `invoke should return failure when authService fails`() = runTest {
        val medicineId = "1"
        val updatedField = UpdatedField(UpdatableFields.NAME, "Old", "New")
        coEvery { authService.getAuthUser() } returns DataResult.Failure(Exception())

        val result = updateMedicineUseCase(medicineId, updatedField)

        assertTrue(result is DataResult.Failure)
        coVerify(exactly = 0) {
            medicineRepository.updateMedicineWithHistory(any(), any())
        }
    }

    @Test
    fun `invoke should call updateMedicineWithHistory when authService succeeds`() = runTest {
        val medicineId = "1"
        val updatedField = UpdatedField(UpdatableFields.NAME, "Old", "New")
        val authUser = AuthUser(uid = "user1", email = "test@test.com", displayName = "Tester")
        coEvery { authService.getAuthUser() } returns DataResult.Success(authUser)

        val expectedResult = DataResult.Success(Unit)
        coEvery {
            medicineRepository.updateMedicineWithHistory(
                medicineId,
                any()
            )
        } returns expectedResult

        val result = updateMedicineUseCase(medicineId, updatedField)

        assertEquals(expectedResult, result)
        coVerify(exactly = 1) {
            medicineRepository.updateMedicineWithHistory(
                medicineId = medicineId,
                history = match {
                    it.medicineId == medicineId &&
                            it.userId == authUser.uid &&
                            it.updatedField == updatedField
                }
            )
        }
    }
}
