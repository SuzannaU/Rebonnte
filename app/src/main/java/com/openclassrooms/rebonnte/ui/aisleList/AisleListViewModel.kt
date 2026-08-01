package com.openclassrooms.rebonnte.ui.aisleList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import com.openclassrooms.rebonnte.ui.model.AisleUi
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

    fun addRandomAisle() {
//        val currentAisles: MutableList<AisleUi> = ArrayList((_uiState as? AisleListScreenState.AislesFound)?.aisles)
//        currentAisles.add(AisleUi(currentAisles.size + 1))
    }
}

