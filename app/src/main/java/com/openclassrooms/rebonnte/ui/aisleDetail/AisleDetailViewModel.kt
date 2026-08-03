package com.openclassrooms.rebonnte.ui.aisleDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.ui.model.AisleUi
import com.openclassrooms.rebonnte.ui.model.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AisleDetailViewModel(
    private val aisleRepository: AisleRepository,
    private val medicineRepository: MedicineRepository,
    private val aisleId: String,
) : ViewModel() {

    private var _uiState = MutableStateFlow<AisleDetailScreenState>(AisleDetailScreenState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadAisle()
    }

    fun loadAisle() {
        viewModelScope.launch {
            _uiState.value = AisleDetailScreenState.Loading
            val aisle =
                aisleRepository.getAisleById(aisleId)?.toUi() ?: AisleUi(id = "id", number = 999)
            medicineRepository.getMedicinesByAisleId(aisleId).collect { medicines ->
                val medicinesUi = medicines.map { medicine ->
                    medicine.toUi()
                }

                _uiState.value = AisleDetailScreenState.AisleFound(
                    aisle,
                    medicinesUi,
                )
            }
        }
    }
}