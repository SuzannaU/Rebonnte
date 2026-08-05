package com.openclassrooms.rebonnte.ui.aisleList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import com.openclassrooms.rebonnte.ui.model.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AisleListViewModel(
    private val aisleRepository: AisleRepository,
) : ViewModel() {

    private var _uiState = MutableStateFlow<AisleListScreenState>(AisleListScreenState.Loading)
    val uiState = _uiState.asStateFlow()

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

    fun addAisle(aisleNumber : String) {        // TODO validate if aislenumber is a digit and display error message
        viewModelScope.launch {
            val existingAisle = aisleRepository.getAisleByNumber(aisleNumber = aisleNumber)
            if (existingAisle != null) {
                _uiState.value = AisleListScreenState.Error(R.string.error_aisle_exists)
            } else {
                aisleRepository.addAisle(Aisle(number = aisleNumber))
            }
        }
    }
}

