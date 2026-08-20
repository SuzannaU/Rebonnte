package com.openclassrooms.rebonnte.ui.aisleDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.rebonnte.domain.useCase.medicine.GetMedicinesByAisleUseCase
import com.openclassrooms.rebonnte.domain.util.DataResult
import com.openclassrooms.rebonnte.ui.DispatcherProvider
import com.openclassrooms.rebonnte.ui.util.toUi
import com.openclassrooms.rebonnte.ui.util.toErrorMessageId
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

    private fun loadAisle() {
        viewModelScope.launch(dispatcher.io) {
            _uiState.value = AisleDetailScreenState.Loading
            getMedicinesByAisle(aisleNumber)
                .collect { result ->
                    when (result) {
                        is DataResult.Success -> {
                            val medicinesUi = result.data.map { medicine ->
                                medicine.toUi()
                            }
                            _uiState.value = AisleDetailScreenState.AisleFound(
                                aisleNumber, medicinesUi,
                            )
                        }

                        is DataResult.Failure -> {
                            _uiState.value = AisleDetailScreenState.Error(
                                errorMessageId = result.exception.toErrorMessageId()
                            )
                        }
                    }
                }
        }
    }
}