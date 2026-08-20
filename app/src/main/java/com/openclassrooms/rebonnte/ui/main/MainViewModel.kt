package com.openclassrooms.rebonnte.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.rebonnte.domain.model.AuthUser
import com.openclassrooms.rebonnte.domain.model.User
import com.openclassrooms.rebonnte.domain.service.AuthService
import com.openclassrooms.rebonnte.domain.useCase.user.CheckUserExistsUseCase
import com.openclassrooms.rebonnte.domain.useCase.user.SaveUserToDbUseCase
import com.openclassrooms.rebonnte.domain.util.DataResult
import com.openclassrooms.rebonnte.ui.DispatcherProvider
import com.openclassrooms.rebonnte.ui.util.toErrorMessageId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val dispatcher: DispatcherProvider,
    private val saveUserToDb: SaveUserToDbUseCase,
    private val checkUserExists: CheckUserExistsUseCase,
    private val authService: AuthService,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch(dispatcher.main) {
            authService.authState
                .catch { _uiState.value = _uiState.value.copy(isAuthConnected = false) }
                .collectLatest { uid ->
                    _uiState.value = _uiState.value.copy(
                        isUserAuthenticated = uid != null,
                        userId = uid,
                        isAuthConnected = true,
                        isLoading = false
                    )
                }
        }
    }

    fun saveNewUserToDb() {
        viewModelScope.launch(dispatcher.io) {

            val authUser = getAuthUser()

            if (authUser == null) {
                return@launch
            } else {
                val userExistsResult = checkUserExists(authUser.uid)
                if (userExistsResult is DataResult.Success && userExistsResult.data) {
                    return@launch
                }
                val creationResult = saveUserToDb(
                    User(
                        id = authUser.uid,
                        username = authUser.displayName,
                        email = authUser.email,
                    )
                )
                when (creationResult) {
                    is DataResult.Success -> {
                        return@launch
                    }

                    is DataResult.Failure -> {
                        creationResult.exception.printStackTrace()
                        _uiState.update { it.copy(errorMessageId = creationResult.exception.toErrorMessageId()) }
                    }
                }
            }
        }
    }


    private suspend fun getAuthUser(): AuthUser? {
        when (val authUserResult = authService.getAuthUser()) {
            is DataResult.Success -> {
                return authUserResult.data
            }

            is DataResult.Failure -> {
                _uiState.update { it.copy(errorMessageId = authUserResult.exception.toErrorMessageId()) }
                return null
            }
        }
    }
}