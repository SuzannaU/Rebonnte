package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.service.AuthService
import com.openclassrooms.rebonnte.domain.useCase.user.LogOutUseCase
import com.openclassrooms.rebonnte.domain.util.DataResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LogOutUseCaseTest {

    private lateinit var authService: AuthService
    private lateinit var logOutUseCase: LogOutUseCase

    @BeforeEach
    fun setUp() {
        authService = mockk()
        logOutUseCase = LogOutUseCase(authService)
    }

    @Test
    fun `invoke should call signOut on authService`() = runTest {
        coEvery { authService.signOut() } returns DataResult.Success(Unit)

        logOutUseCase()

        coVerify(exactly = 1) { authService.signOut() }
    }
}
