package com.openclassrooms.rebonnte.ui.medicineDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.model.UpdatableFields
import com.openclassrooms.rebonnte.domain.model.UpdatedField
import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import com.openclassrooms.rebonnte.domain.repository.HistoryRepository
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.domain.useCase.UpdateMedicineUseCase
import com.openclassrooms.rebonnte.ui.aisleList.AddAisleState
import com.openclassrooms.rebonnte.ui.model.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MedicineDetailViewModel(
    private val medicineRepository: MedicineRepository,
    private val historyRepository: HistoryRepository,
    private val aisleRepository: AisleRepository,
    private val updateMedicineUseCase: UpdateMedicineUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val medicineId: String = savedStateHandle["medicineId"] ?: ""

    private var _uiState = MutableStateFlow<MedicineDetailState>(MedicineDetailState.Loading)
    val uiState = _uiState.asStateFlow()

    private var _formState = MutableStateFlow(EditMedicineFormState())
    val formState = _formState.asStateFlow()

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
                _formState.update {
                    it.copy(
                        name = medicineUi.name,
                        aisleNumber = medicineUi.aisleNumber,
                        stock = medicineUi.currentStock
                    )
                }
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
        if (input.isBlank()) {
            _formState.update {
                it.copy(
                    formError = EditMedicineFormErrorState(nameBlankError = true)
                )
            }
            return
        }
        if (input.length > 25) {
            _formState.update {
                it.copy(
                    formError = EditMedicineFormErrorState(nameLengthError = true)
                )
            }
            return
        }
        _formState.update {
            it.copy(
                name = input,
                formError = EditMedicineFormErrorState(),
            )
        }
        updateField(
            updatedField = UpdatedField(
                field = UpdatableFields.NAME,
                oldValue = initialMedicine.name,
                newValue = input,
            )
        )
    }

    fun updateAisle(input: String) {
        if (!input.all { it.isDigit() }) {
            _formState.update {
                it.copy(
                    formError = EditMedicineFormErrorState(aisleDigitError = true)
                )
            }
            return
        }
        viewModelScope.launch {
            if (!aisleExists(input)) {
                _formState.update {
                    it.copy(
                        formError = EditMedicineFormErrorState(aisleDoesNotExistError = true)
                    )
                }
                return@launch
            }
            _formState.update {
                it.copy(
                    aisleNumber = input,
                    formError = EditMedicineFormErrorState(),
                )
            }
            updateField(
                updatedField = UpdatedField(
                    field = UpdatableFields.AISLE,
                    oldValue = initialMedicine.aisleNumber,
                    newValue = input,
                )
            )
        }
    }

    fun updateStock(input: String) {
        if (input.isBlank()) {
            _formState.update {
                it.copy(
                    formError = EditMedicineFormErrorState(stockBlankError = true)
                )
            }
            return
        }
        if (!input.all { it.isDigit() }) {
            _formState.update {
                it.copy(
                    formError = EditMedicineFormErrorState(stockDigitError = true)
                )
            }
            return
        }
        _formState.update {
            it.copy(
                stock = input,
                formError = EditMedicineFormErrorState(),
            )
        }
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
            _formState.update { it.copy(isSuccess = true) }
            loadMedicine()
        }
    }

    private suspend fun aisleExists(aisleNumber: String): Boolean {
        return aisleRepository.getAisleByNumber(aisleNumber = aisleNumber) != null
    }

    fun resetAddAisleState() {
        _formState.value = EditMedicineFormState()
    }


    fun archiveMedicine() {
        viewModelScope.launch {
            medicineRepository.archiveMedicineById(medicineId)
        }
    }
}