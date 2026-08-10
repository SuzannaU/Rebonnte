package com.openclassrooms.rebonnte.ui.aisleDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.rebonnte.domain.useCase.GetMedicinesByAisleUseCase
import com.openclassrooms.rebonnte.ui.DispatcherProvider
import com.openclassrooms.rebonnte.ui.model.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AisleDetailViewModel(
    private val getMedicinesByAisle: GetMedicinesByAisleUseCase,
    private val dispatcher: DispatcherProvider,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val aisleNumber: String = savedStateHandle["aisleNumber"] ?: ""

    private var _uiState = MutableStateFlow<AisleDetailScreenState>(AisleDetailScreenState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadAisle()
    }

    fun loadAisle() {
        viewModelScope.launch(dispatcher.io) {
            _uiState.value = AisleDetailScreenState.Loading
            getMedicinesByAisle(aisleNumber).collect { medicines ->
                val medicinesUi = medicines.map { medicine ->
                    medicine.toUi()
                }
                _uiState.value = AisleDetailScreenState.AisleFound(
                    aisleNumber, medicinesUi,
                )
            }
        }
    }
}