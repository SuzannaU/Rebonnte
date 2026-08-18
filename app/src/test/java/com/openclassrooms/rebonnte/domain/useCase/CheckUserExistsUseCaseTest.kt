package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.model.User
import com.openclassrooms.rebonnte.domain.repository.UserRepository
import com.openclassrooms.rebonnte.domain.useCase.user.CheckUserExistsUseCase
import com.openclassrooms.rebonnte.domain.util.DataResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CheckUserExistsUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var checkUserExistsUseCase: CheckUserExistsUseCase

    @BeforeEach
    fun setUp() {
        userRepository = mockk()
        checkUserExistsUseCase = CheckUserExistsUseCase(userRepository)
    }

    @Test
    fun `invoke should return Success(true) when user exists`() = runTest {
        val userId = "user1"
        coEvery { userRepository.getUserById(userId) } returns DataResult.Success(
            User(
                id = userId,
                email = "t@t.com",
                username = "u"
            )
        )

        val result = checkUserExistsUseCase(userId)

        assertEquals(DataResult.Success(true), result)
    }

    @Test
    fun `invoke should return Success(false) when user does not exist`() = runTest {
        val userId = "user1"
        coEvery { userRepository.getUserById(userId) } returns DataResult.Success(null)

        val result = checkUserExistsUseCase(userId)

        assertEquals(DataResult.Success(false), result)
    }

    @Test
    fun `invoke should return Failure when repository fails`() = runTest {
        val userId = "user1"
        val exception = Exception("Error")
        coEvery { userRepository.getUserById(userId) } returns DataResult.Failure(exception)

        val result = checkUserExistsUseCase(userId)

        assertTrue(result is DataResult.Failure)
        assertEquals(exception, (result as DataResult.Failure).exception)
    }
}
