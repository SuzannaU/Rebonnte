package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.service.AuthService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
    fun `invoke should call signOut on authService`() {
        every { authService.signOut() } returns Unit

        logOutUseCase()

        verify(exactly = 1) { authService.signOut() }
    }
}
