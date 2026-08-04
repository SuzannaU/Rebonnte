package com.openclassrooms.rebonnte.ui.addMedicine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import com.openclassrooms.rebonnte.domain.useCase.AddMedicineUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddMedicineViewModel(
    private val aisleRepository: AisleRepository,
    private val addMedicineUseCase: AddMedicineUseCase,
) : ViewModel() {

    private var _formState = MutableStateFlow(FormState())
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

    fun addMedicine() {
        if (!validate()) return
        _saveState.value = SaveState.Loading

        viewModelScope.launch {
            val aisleNumber = _formState.value.aisleNumber
            val aisleExists = aisleRepository.getAisleByNumber(aisleNumber)
            println(aisleExists)
            if (aisleExists == null) {
                _formState.update {
                    it.copy(formErrors = it.formErrors.copy(aisleDoesNotExistError = true))
                }
                _saveState.value = SaveState.Idle
                return@launch
            }

            val medicine = Medicine(
                id = _formState.value.id,
                name = _formState.value.name,
                aisleNumber = aisleNumber,
                currentStock = _formState.value.currentStock.toInt()
            )

            addMedicineUseCase.execute(medicine)
            _saveState.value = SaveState.MedicineSaved
        }
    }

    private fun validate(): Boolean {
        val state = _formState.value

        val nameError = state.name.isBlank()
        val nameLengthError = state.name.length > 25
        val stockDigitError =
            state.currentStock.isBlank() || !state.currentStock.all { it.isDigit() }
        val aisleDigitError = state.aisleNumber.isBlank() || !state.aisleNumber.all { it.isDigit() }

        // TODO add logic to check if aisle exists

        val errors = FormErrorState(
            nameError = nameError,
            nameLengthError = nameLengthError,
            stockDigitError = stockDigitError,
            aisleDigitError = aisleDigitError,
        )

        _formState.update { it.copy(formErrors = errors) }

        return !nameError && !nameLengthError && !stockDigitError && !aisleDigitError
    }
}