package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.model.AuthUser
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.domain.service.AuthService
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

class ArchiveMedicineUseCaseTest {

    private lateinit var medicineRepository: MedicineRepository
    private lateinit var authService: AuthService
    private lateinit var archiveMedicineUseCase: ArchiveMedicineUseCase

    @BeforeEach
    fun setUp() {
        medicineRepository = mockk()
        authService = mockk()
        archiveMedicineUseCase = ArchiveMedicineUseCase(medicineRepository, authService)
    }

    @Test
    fun `invoke should return failure when authService fails`() = runTest {
        val medicineId = "1"
        every { authService.getAuthUser() } throws Exception()

        val result = archiveMedicineUseCase(medicineId)

        assertTrue(result is DataResult.Failure)
        coVerify(exactly = 0) {
            medicineRepository.archiveMedicineWithHistory(any(), any())
        }
    }

    @Test
    fun `invoke should call archiveMedicineWithHistory when authService succeeds`() = runTest {
        val medicineId = "test_id"
        val authUser = AuthUser(uid = "user1", email = "test@test.com", displayName = "Tester")
        every { authService.getAuthUser() } returns authUser

        val expectedResult = DataResult.Success(Unit)
        coEvery {
            medicineRepository.archiveMedicineWithHistory(
                medicineId,
                any()
            )
        } returns expectedResult

        val result = archiveMedicineUseCase(medicineId)

        assertEquals(expectedResult, result)
        coVerify(exactly = 1) {
            medicineRepository.archiveMedicineWithHistory(
                medicineId = medicineId,
                history = match {
                    it.medicineId == medicineId &&
                            it.userId == authUser.uid &&
                            it.isArchiving
                }
            )
        }
    }
}
