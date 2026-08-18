package com.openclassrooms.rebonnte.ui.medicineDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.model.UpdatableFields
import com.openclassrooms.rebonnte.domain.model.UpdatedField
import com.openclassrooms.rebonnte.domain.useCase.aisle.CheckAisleExistsUseCase
import com.openclassrooms.rebonnte.domain.useCase.medicine.GetMedicineByIdUseCase
import com.openclassrooms.rebonnte.domain.useCase.medicine.UpdateMedicineUseCase
import com.openclassrooms.rebonnte.domain.util.DataResult
import com.openclassrooms.rebonnte.ui.DispatcherProvider
import com.openclassrooms.rebonnte.ui.util.toErrorMessageId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditMedicineViewModel(
    private val getMedicineById: GetMedicineByIdUseCase,
    private val updateMedicine: UpdateMedicineUseCase,
    private val checkAisleExists: CheckAisleExistsUseCase,
    private val dispatcher: DispatcherProvider,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val medicineId: String = savedStateHandle["medicineId"] ?: ""

    private var _formState = MutableStateFlow(EditMedicineFormState())
    val formState = _formState.asStateFlow()

    private var initialMedicine: Medicine? = null

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch(dispatcher.io) {
            val medicine = getMedicineById(medicineId).first()
            if (medicine != null) {
                initialMedicine = medicine
                _formState.update {
                    it.copy(
                        name = medicine.name,
                        aisleNumber = medicine.aisleNumber,
                        stock = medicine.currentStock.toString()
                    )
                }
            }
        }
    }

    fun onSaveName(newName: String) {
        val medicine = initialMedicine ?: return

        if (newName.isBlank()) {
            _formState.update {
                it.copy(formError = EditMedicineFormErrorState(nameBlankError = true))
            }
            return
        }
        if (newName.length > 25) {
            _formState.update {
                it.copy(formError = EditMedicineFormErrorState(nameLengthError = true))
            }
            return
        }

        updateField(
            UpdatedField(
                field = UpdatableFields.NAME,
                oldValue = medicine.name,
                newValue = newName,
            )
        )
    }

    fun onSaveAisle(newAisle: String) {
        val medicine = initialMedicine ?: return

        if (newAisle.isBlank() || !newAisle.all { it.isDigit() }) {
            _formState.update {
                it.copy(formError = EditMedicineFormErrorState(aisleDigitError = true))
            }
            return
        }

        viewModelScope.launch(dispatcher.io) {
            val existsResult = checkAisleExists(newAisle)
            if (existsResult is DataResult.Success && !existsResult.data) {
                _formState.update {
                    it.copy(
                        formError = EditMedicineFormErrorState(aisleDoesNotExistError = true)
                    )
                }
                return@launch
            }
            updateField(
                UpdatedField(
                    field = UpdatableFields.AISLE,
                    oldValue = medicine.aisleNumber,
                    newValue = newAisle,
                )
            )
        }
    }

    fun onSaveStock(newStock: String) {
        val medicine = initialMedicine ?: return

        if (newStock.isBlank() || !newStock.all { it.isDigit() }) {
            _formState.update {
                it.copy(formError = EditMedicineFormErrorState(stockBlankError = true))
            }
            return
        }

        updateField(
            UpdatedField(
                field = UpdatableFields.STOCK,
                oldValue = medicine.currentStock.toString(),
                newValue = newStock,
            )
        )
    }

    private fun updateField(updatedField: UpdatedField) {
        viewModelScope.launch(dispatcher.io) {
            val result = updateMedicine(
                medicineId = medicineId,
                updatedField = updatedField,
            )
            when (result) {
                is DataResult.Success -> {
                    _formState.update { it.copy(isSuccess = true) }
                }
                is DataResult.Failure -> {
                    _formState.update { it.copy(errorId = result.exception.toErrorMessageId()) }
                }
            }
        }
    }

    fun resetSuccessState() {
        _formState.update { 
            it.copy(
                isSuccess = false,
                formError = EditMedicineFormErrorState(),
                errorId = null
            ) 
        }
    }
}
