package com.openclassrooms.rebonnte.ui.addMedicine

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.useCase.medicine.AddMedicineUseCase
import com.openclassrooms.rebonnte.domain.useCase.aisle.CheckAisleExistsUseCase
import com.openclassrooms.rebonnte.domain.util.DataResult
import com.openclassrooms.rebonnte.ui.DispatcherProvider
import com.openclassrooms.rebonnte.ui.util.toErrorMessageId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddMedicineViewModel(
    private val checkAisleExists: CheckAisleExistsUseCase,
    private val addMedicine: AddMedicineUseCase,
    private val dispatcher: DispatcherProvider,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val initialAisleNumber: String = savedStateHandle["aisleNumber"] ?: ""

    private var _formState = MutableStateFlow(FormState(aisleNumber = initialAisleNumber))
    val formState = _formState.asStateFlow()

    private var _saveState = MutableStateFlow<SaveState>(SaveState.Idle)
    val saveState = _saveState.asStateFlow()

    fun updateName(input: String) {
        _formState.update {
            it.copy(
                name = input,
                formErrors = it.formErrors.copy(nameError = false)
            )
        }
    }

    fun updateAisle(input: String) {
        _formState.update {
            it.copy(
                aisleNumber = input,
                formErrors = it.formErrors.copy(aisleDigitError = false)
            )
        }
    }

    fun updateStock(input: String) {
        _formState.update {
            it.copy(
                currentStock = input,
                formErrors = it.formErrors.copy(stockDigitError = false)
            )
        }
    }

    fun onAddMedicine() {
        _saveState.value = SaveState.Loading

        viewModelScope.launch(dispatcher.io) {
            if (!validate()) {
                _saveState.value = SaveState.Idle
                return@launch
            }

            val medicine = Medicine(
                id = _formState.value.id,
                name = _formState.value.name,
                aisleNumber = _formState.value.aisleNumber,
                currentStock = _formState.value.currentStock.toInt()
            )

            when (val result = addMedicine(medicine)) {
                is DataResult.Success -> {
                    _saveState.value = SaveState.MedicineSaved
                }
                is DataResult.Failure -> {
                    _saveState.value = SaveState.Error(result.exception.toErrorMessageId())
                }
            }
        }
    }

    private suspend fun validate(): Boolean {
        val state = _formState.value

        val nameError = state.name.isBlank()
        val nameLengthError = state.name.length > 25
        val stockDigitError =
            state.currentStock.isBlank() || !state.currentStock.all { it.isDigit() }
        val aisleDigitError = state.aisleNumber.isBlank() || !state.aisleNumber.all { it.isDigit() }
        var aisleVerificationError = false

        val aisleDoesNotExistError = if (!aisleDigitError) {
            when (val result = checkAisleExists(aisleNumber = state.aisleNumber)) {
                is DataResult.Success -> !result.data
                is DataResult.Failure -> {
                    aisleVerificationError = true
                    false
                }
            }
        } else {
            false
        }

        val errors = FormErrorState(
            nameError = nameError,
            nameLengthError = nameLengthError,
            stockDigitError = stockDigitError,
            aisleDigitError = aisleDigitError,
            aisleDoesNotExistError = aisleDoesNotExistError,
            aisleVerificationError = aisleVerificationError,
        )

        _formState.update { it.copy(formErrors = errors) }

        return !nameError && !nameLengthError && !stockDigitError && !aisleDigitError && !aisleDoesNotExistError && !aisleVerificationError
    }
}