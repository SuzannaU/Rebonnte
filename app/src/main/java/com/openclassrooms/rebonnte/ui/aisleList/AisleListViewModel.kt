package com.openclassrooms.rebonnte.ui.aisleList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import com.openclassrooms.rebonnte.ui.model.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AisleListViewModel(
    private val aisleRepository: AisleRepository,
) : ViewModel() {

    private var _uiState = MutableStateFlow<AisleListScreenState>(AisleListScreenState.Loading)
    val uiState = _uiState.asStateFlow()

    private var _addAisleState = MutableStateFlow(AddAisleState())
    val addAisleState = _addAisleState.asStateFlow()

    init {
        loadAisles()
    }

    fun loadAisles() {
        viewModelScope.launch {
            _uiState.value = AisleListScreenState.Loading
            aisleRepository.getAisles().collect { aisles ->
                val aislesUi = aisles.map { aisle ->
                    aisle.toUi()
                }
                _uiState.value = AisleListScreenState.AislesFound(aislesUi)
            }
        }
    }

    fun addAisle(aisleNumber: String) {
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

        viewModelScope.launch {
            val existingAisle = aisleRepository.getAisleByNumber(aisleNumber = aisleNumber)
            if (existingAisle != null) {
                _addAisleState.update { it.copy(aisleExistsError = true) }
                return@launch
            } else {
                aisleRepository.addAisle(Aisle(number = aisleNumber))
                _addAisleState.update { it.copy(isSuccess = true) }
            }
        }
    }

    fun resetAddAisleState() {
        _addAisleState.value = AddAisleState()
    }
}