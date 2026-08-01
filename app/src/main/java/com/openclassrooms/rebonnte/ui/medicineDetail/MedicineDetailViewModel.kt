package com.openclassrooms.rebonnte.ui.medicineDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.rebonnte.domain.repository.HistoryRepository
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.ui.model.MedicineUi
import com.openclassrooms.rebonnte.ui.model.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MedicineDetailViewModel(
    private val medicineRepository: MedicineRepository,
    private val historyRepository: HistoryRepository,
    private val medicineId : String,
) : ViewModel() {

    private var _uiState = MutableStateFlow<MedicineDetailState>(MedicineDetailState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadMedicine()
    }

    fun loadMedicine() {
        viewModelScope.launch {
            _uiState.value = MedicineDetailState.Loading
            val medicine = medicineRepository.getMedicineById(medicineId = medicineId)?.toUi()
            historyRepository.getHistoryByMedicineId(medicineId = medicineId).collect { histories ->
                val historiesUi = histories.map { history ->
                    history.toUi()
                }

                _uiState.value = MedicineDetailState.MedicineFound(
                    medicine = medicine ?: MedicineUi("id", "name", 999),
                    histories = historiesUi,
                )
            }
        }
    }
}