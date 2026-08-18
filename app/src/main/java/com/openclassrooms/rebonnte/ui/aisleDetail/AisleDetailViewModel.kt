package com.openclassrooms.rebonnte.ui.aisleDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.rebonnte.domain.useCase.medicine.GetMedicinesByAisleUseCase
import com.openclassrooms.rebonnte.ui.DispatcherProvider
import com.openclassrooms.rebonnte.ui.util.toUi
import com.openclassrooms.rebonnte.ui.util.toErrorMessageId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
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

    private fun loadAisle() {
        viewModelScope.launch(dispatcher.io) {
            _uiState.value = AisleDetailScreenState.Loading
            getMedicinesByAisle(aisleNumber)
                .catch { e ->
                    _uiState.value = AisleDetailScreenState.Error(errorMessageId = e.toErrorMessageId())
                }
                .collect { medicines ->
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