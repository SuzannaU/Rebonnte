package com.openclassrooms.rebonnte.ui.aisleDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.ui.model.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AisleDetailViewModel(
    private val medicineRepository: MedicineRepository,
    val aisleNumber: String,
) : ViewModel() {

    private var _uiState = MutableStateFlow<AisleDetailScreenState>(AisleDetailScreenState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadAisle()
    }

    fun loadAisle() {
        viewModelScope.launch {
            _uiState.value = AisleDetailScreenState.Loading
            medicineRepository.getMedicinesByAisleNumber(aisleNumber).collect { medicines ->
                val medicinesUi = medicines.map { medicine ->
                    medicine.toUi()
                }
                _uiState.value = AisleDetailScreenState.AisleFound(
                    medicinesUi,
                )
            }
        }
    }
}