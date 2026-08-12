package com.openclassrooms.rebonnte.ui.aisleList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.rebonnte.domain.useCase.AddAisleUseCase
import com.openclassrooms.rebonnte.domain.useCase.CheckAisleExistsUseCase
import com.openclassrooms.rebonnte.domain.useCase.GetAislesUseCase
import com.openclassrooms.rebonnte.domain.useCase.LogOutUseCase
import com.openclassrooms.rebonnte.domain.util.DataResult
import com.openclassrooms.rebonnte.ui.DispatcherProvider
import com.openclassrooms.rebonnte.ui.model.toUi
import com.openclassrooms.rebonnte.ui.util.toErrorMessageId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AisleListViewModel(
    private val getAisles: GetAislesUseCase,
    private val checkAisleExists: CheckAisleExistsUseCase,
    private val addAisle: AddAisleUseCase,
    private val logOut: LogOutUseCase,
    private val dispatcher: DispatcherProvider,
) : ViewModel() {

    private var _uiState = MutableStateFlow<AisleListScreenState>(AisleListScreenState.Loading)
    val uiState = _uiState.asStateFlow()

    private var _addAisleState = MutableStateFlow(AddAisleState())
    val addAisleState = _addAisleState.asStateFlow()

    init {
        loadAisles()
    }

    fun loadAisles() {
        viewModelScope.launch(dispatcher.io) {
            _uiState.value = AisleListScreenState.Loading
            getAisles()
                .catch { e ->
                    _uiState.value = AisleListScreenState.Error(e.toErrorMessageId())
                }
                .collect { aisles ->
                    val aislesUi = aisles.map { aisle ->
                        aisle.toUi()
                    }
                    _uiState.value = AisleListScreenState.AislesFound(aislesUi)
                }
        }
    }

    fun onAddAisle(aisleNumber: String) {
        _addAisleState.update {
            it.copy(
                aisleBlankError = false,
                aisleDigitError = false,
                aisleExistsError = false
            )
        }

        if (aisleNumber.isBlank()) {
            _addAisleState.update { it.copy(aisleBlankError = true) }
            return
        }
        if (!aisleNumber.all { it.isDigit() }) {
            _addAisleState.update { it.copy(aisleDigitError = true) }
            return
        }

        viewModelScope.launch(dispatcher.io) {
            val existsResult = checkAisleExists(aisleNumber)
            if (existsResult is DataResult.Success && existsResult.data) {
                _addAisleState.update { it.copy(aisleExistsError = true) }
                return@launch
            } else if (existsResult is DataResult.Failure) {
                _uiState.value = AisleListScreenState.Error(existsResult.exception.toErrorMessageId())
                return@launch
            }

            when (val result = addAisle(aisleNumber = aisleNumber)) {
                is DataResult.Success -> {
                    _addAisleState.update { it.copy(isSuccess = true) }
                }

                is DataResult.Failure -> {
                    _uiState.value = AisleListScreenState.Error(result.exception.toErrorMessageId())
                }
            }
        }
    }

    fun resetAddAisleState() {
        _addAisleState.value = AddAisleState()
    }

    fun onLogout() {
        logOut()
    }
}