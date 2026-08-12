package com.openclassrooms.rebonnte.ui.medicineDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.model.UpdatableFields
import com.openclassrooms.rebonnte.domain.model.UpdatedField
import com.openclassrooms.rebonnte.domain.useCase.ArchiveMedicineUseCase
import com.openclassrooms.rebonnte.domain.useCase.CheckAisleExistsUseCase
import com.openclassrooms.rebonnte.domain.useCase.GetHistoryByMedicineUseCase
import com.openclassrooms.rebonnte.domain.useCase.GetMedicineByIdUseCase
import com.openclassrooms.rebonnte.domain.useCase.GetUsernameByIdUseCase
import com.openclassrooms.rebonnte.domain.useCase.UpdateMedicineUseCase
import com.openclassrooms.rebonnte.domain.util.DataResult
import com.openclassrooms.rebonnte.ui.DispatcherProvider
import com.openclassrooms.rebonnte.ui.model.toUi
import com.openclassrooms.rebonnte.ui.util.toErrorMessageId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MedicineDetailViewModel(
    private val getMedicineById: GetMedicineByIdUseCase,
    private val updateMedicine: UpdateMedicineUseCase,
    private val getHistoryByMedicine: GetHistoryByMedicineUseCase,
    private val getUsernameById: GetUsernameByIdUseCase,
    private val checkAisleExists: CheckAisleExistsUseCase,
    private val archiveMedicine: ArchiveMedicineUseCase,
    private val dispatcher: DispatcherProvider,
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
        viewModelScope.launch(dispatcher.io) {
            _uiState.value = MedicineDetailState.Loading
            when (val result = getMedicineById(medicineId)) {
                is DataResult.Success -> {
                    onLoadSuccess(result.data)
                }

                is DataResult.Failure -> {
                    onLoadFailure(result.exception)
                }
            }
        }
    }

    private suspend fun onLoadSuccess(medicine: Medicine?) {
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
            getHistoryByMedicine(medicineId = medicineId)
                .collect { histories ->
                    val historiesUi = histories.map { history ->
                        history.toUi(getUsernameById(history.userId))
                    }
                    _uiState.value = MedicineDetailState.MedicineFound(
                        medicine = medicineUi,
                        histories = historiesUi,
                    )
                }
        } else {
            _uiState.value = MedicineDetailState.MedicineNotFound
        }
    }

    private fun onLoadFailure(exception: Throwable) {
        _uiState.value = MedicineDetailState.Error(exception.toErrorMessageId())

    }

    fun onNameChange(input: String) {
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

    fun onAisleChange(input: String) {
        if (!input.all { it.isDigit() }) {
            _formState.update {
                it.copy(
                    formError = EditMedicineFormErrorState(aisleDigitError = true)
                )
            }
            return
        }
        viewModelScope.launch(dispatcher.io) {
            val existsResult = checkAisleExists(input)
            if (existsResult is DataResult.Success && !existsResult.data) {
                _formState.update {
                    it.copy(
                        formError = EditMedicineFormErrorState(aisleDoesNotExistError = true)
                    )
                }
                return@launch
            } else if (existsResult is DataResult.Failure) {
                _uiState.value =
                    MedicineDetailState.Error(existsResult.exception.toErrorMessageId())
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

    fun onStockChange(input: String) {
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
        viewModelScope.launch(dispatcher.io) {
            _uiState.value = MedicineDetailState.Loading
            val result = updateMedicine(
                medicineId = state.medicine.id,
                updatedField = updatedField,
            )
            when (result) {
                is DataResult.Success -> {
                    _formState.update { it.copy(isSuccess = true) }
                    loadMedicine()
                }

                is DataResult.Failure -> {
                    _uiState.value = MedicineDetailState.Error(result.exception.toErrorMessageId())
                }
            }
        }
    }

    fun resetAddAisleState() {
        _formState.value = EditMedicineFormState()
    }


    fun onArchiveClick() {
        viewModelScope.launch {
            when (val result = archiveMedicine(medicineId)) {
                is DataResult.Success -> {
                    // Success is handled by the UI navigating back or refreshing
                }

                is DataResult.Failure -> {
                    _uiState.value = MedicineDetailState.Error(result.exception.toErrorMessageId())
                }
            }
        }
    }
}