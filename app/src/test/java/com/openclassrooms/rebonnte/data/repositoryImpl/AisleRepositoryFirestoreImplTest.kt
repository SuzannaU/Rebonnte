package com.openclassrooms.rebonnte.data.repositoryImpl

import com.openclassrooms.rebonnte.data.datasource.AisleDataSource
import com.openclassrooms.rebonnte.data.dto.AisleDto
import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.exception.UnknownException
import com.openclassrooms.rebonnte.domain.util.DataResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AisleRepositoryFirestoreImplTest {

    private lateinit var aisleDataSource: AisleDataSource
    private lateinit var aisleRepository: AisleRepositoryFirestoreImpl

    @BeforeEach
    fun setUp() {
        aisleDataSource = mockk()
        aisleRepository = AisleRepositoryFirestoreImpl(aisleDataSource)
    }

    @Test
    fun `getAisleByNumber returns Success with aisle when aisle exists`() = runTest {
        val aisleNumber = "10"
        val aisleDto = AisleDto(id = aisleNumber)
        coEvery { aisleDataSource.getAisleByNumber(aisleNumber) } returns aisleDto

        val result = aisleRepository.getAisleByNumber(aisleNumber)

        assertTrue(result is DataResult.Success)
        assertEquals(aisleNumber, (result as DataResult.Success).data?.number)
        coVerify(exactly = 1) { aisleDataSource.getAisleByNumber(match { it == aisleNumber }) }
    }

    @Test
    fun `getAisleByNumber returns Success with null when aisle does not exist`() = runTest {
        val aisleNumber = "10"
        coEvery { aisleDataSource.getAisleByNumber(aisleNumber) } returns null

        val result = aisleRepository.getAisleByNumber(aisleNumber)

        assertTrue(result is DataResult.Success)
        assertEquals(null, (result as DataResult.Success).data)
        coVerify(exactly = 1) { aisleDataSource.getAisleByNumber(match { it == aisleNumber }) }
    }

    @Test
    fun `getAisleByNumber returns failure when datasource throws`() = runTest {
        val aisleNumber = "10"
        coEvery { aisleDataSource.getAisleByNumber(any()) } throws Exception()

        val result = aisleRepository.getAisleByNumber(aisleNumber)

        assertTrue(result is DataResult.Failure)
        coVerify(exactly = 1) { aisleDataSource.getAisleByNumber(match { it == aisleNumber }) }
    }

    @Test
    fun `getAisles returns sorted aisles`() = runTest {
        val aisleDtos = listOf(
            AisleDto(id = "10"),
            AisleDto(id = "2"),
            AisleDto(id = "1")
        )
        every { aisleDataSource.getAisles() } returns flowOf(aisleDtos)

        val result = aisleRepository.getAisles().first()

        assertTrue(result is DataResult.Success)
        val data = (result as DataResult.Success).data
        assertEquals(3, data.size)
        assertEquals("1", data[0].number)
        assertEquals("2", data[1].number)
        assertEquals("10", data[2].number)
        coVerify(exactly = 1) { aisleDataSource.getAisles() }
    }

    @Test
    fun `getAisles returns failure when datasource fails`() = runTest {
        val exception = Exception("test")
        every { aisleDataSource.getAisles() } returns flow { throw exception }

        val result = aisleRepository.getAisles().first()

        assertTrue(result is DataResult.Failure)
        assertTrue((result as DataResult.Failure).exception is UnknownException)
        assertEquals("test", result.exception.message)
        coVerify(exactly = 1) { aisleDataSource.getAisles() }
    }

    @Test
    fun `addAisle returns Success when datasource succeeds`() = runTest {
        val aisle = Aisle(number = "5")
        coEvery { aisleDataSource.saveAisle(any()) } returns Unit

        val result = aisleRepository.addAisle(aisle)

        assertTrue(result is DataResult.Success)
        coVerify(exactly = 1) { aisleDataSource.saveAisle(match { it.id == "5" }) }
    }

    @Test
    fun `addAisle returns Failure when datasource fails`() = runTest {
        val aisle = Aisle(number = "5")
        coEvery { aisleDataSource.saveAisle(any()) } throws Exception()

        val result = aisleRepository.addAisle(aisle)

        assertTrue(result is DataResult.Failure)
        coVerify(exactly = 1) { aisleDataSource.saveAisle(match { it.id == "5" }) }
    }
}
