package com.openclassrooms.rebonnte.ui.main

data class MainUiState(
    val isLoading: Boolean = true,
    val isUserAuthenticated: Boolean = false,
    val userId: String? = null,
    val isAuthConnected: Boolean = false,
    val errorMessageId: Int? = null,
)