package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import com.openclassrooms.rebonnte.domain.useCase.aisle.CheckAisleExistsUseCase
import com.openclassrooms.rebonnte.domain.util.DataResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CheckAisleExistsUseCaseTest {

    private lateinit var aisleRepository: AisleRepository
    private lateinit var checkAisleExistsUseCase: CheckAisleExistsUseCase

    @BeforeEach
    fun setUp() {
        aisleRepository = mockk()
        checkAisleExistsUseCase = CheckAisleExistsUseCase(aisleRepository)
    }

    @Test
    fun `invoke should return Success(true) when aisle exists`() = runTest {
        val aisleNumber = "1"
        coEvery { aisleRepository.getAisleByNumber(aisleNumber) } returns DataResult.Success(
            Aisle(
                number = aisleNumber
            )
        )

        val result = checkAisleExistsUseCase(aisleNumber)

        assertEquals(DataResult.Success(true), result)
    }

    @Test
    fun `invoke should return Success(false) when aisle does not exist`() = runTest {
        val aisleNumber = "1"
        coEvery { aisleRepository.getAisleByNumber(aisleNumber) } returns DataResult.Success(null)

        val result = checkAisleExistsUseCase(aisleNumber)

        assertEquals(DataResult.Success(false), result)
    }

    @Test
    fun `invoke should return Failure when repository fails`() = runTest {
        val aisleNumber = "1"
        val exception = Exception("Error")
        coEvery { aisleRepository.getAisleByNumber(aisleNumber) } returns DataResult.Failure(
            exception
        )

        val result = checkAisleExistsUseCase(aisleNumber)

        assertTrue(result is DataResult.Failure)
        assertEquals(exception, (result as DataResult.Failure).exception)
    }
}
