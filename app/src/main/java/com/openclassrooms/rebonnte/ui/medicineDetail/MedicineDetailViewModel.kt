package com.openclassrooms.rebonnte.ui.medicineDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.model.UpdatableFields
import com.openclassrooms.rebonnte.domain.model.UpdatedField
import com.openclassrooms.rebonnte.domain.repository.HistoryRepository
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.domain.useCase.UpdateMedicineUseCase
import com.openclassrooms.rebonnte.ui.model.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MedicineDetailViewModel(
    private val medicineRepository: MedicineRepository,
    private val historyRepository: HistoryRepository,
    private val updateMedicineUseCase: UpdateMedicineUseCase,
    private val medicineId: String,
) : ViewModel() {

    private var _uiState = MutableStateFlow<MedicineDetailState>(MedicineDetailState.Loading)
    val uiState = _uiState.asStateFlow()

    private var _nameState = MutableStateFlow("")
    private var _aisleState = MutableStateFlow("")
    private var _stockState = MutableStateFlow("")

    lateinit var initialMedicine: Medicine

    init {
        loadMedicine()
    }

    private fun loadMedicine() {
        viewModelScope.launch {
            _uiState.value = MedicineDetailState.Loading
            val medicine = medicineRepository.getMedicineById(medicineId = medicineId)
            if (medicine != null) {
                initialMedicine = medicine
                val medicineUi = medicine.toUi()

                _uiState.value = MedicineDetailState.MedicineFound(
                    medicine = medicineUi,
                    histories = emptyList(),
                )
                _stockState.value = medicineUi.currentStock
                _aisleState.value = medicineUi.aisleNumber
                historyRepository.getHistoryByMedicineId(medicineId = medicineId)
                    .collect { histories ->
                        val historiesUi = histories.map { history ->
                            history.toUi()
                        }
                        _uiState.value = MedicineDetailState.MedicineFound(
                            medicine = medicineUi,
                            histories = historiesUi,
                        )
                    }
            }
        }
    }

    fun updateName(input: String) {
        _nameState.value = input
        updateField(
            updatedField = UpdatedField(
                field = UpdatableFields.NAME,
                oldValue = initialMedicine.name,
                newValue = input,
            )
        )
    }

    fun updateAisle(input: String) {
        if (!input.all { it.isDigit() }) return         // TODO proper form error
        _aisleState.value = input
        updateField(
            updatedField = UpdatedField(
                field = UpdatableFields.AISLE,
                oldValue = initialMedicine.aisleNumber,
                newValue = input,
            )
        )
    }

    fun updateStock(input: String) {
        if (!input.all { it.isDigit() }) return         // TODO proper form error
        _stockState.value = input
        updateField(
            updatedField = UpdatedField(
                field = UpdatableFields.STOCK,
                oldValue = initialMedicine.currentStock.toString(),
                newValue = input,
            )
        )
    }

    private fun updateField(updatedField: UpdatedField) {
        val state = _uiState.value
        if (state !is MedicineDetailState.MedicineFound) return
        viewModelScope.launch {
            _uiState.value = MedicineDetailState.Loading
            updateMedicineUseCase.execute(
                medicineId = state.medicine.id,
                updatedField = updatedField,
            )
            loadMedicine()
        }
    }

    fun deleteMedicine() {
        viewModelScope.launch {
            medicineRepository.deleteMedicineById(medicineId)
        }
    }
}