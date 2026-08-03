package com.openclassrooms.rebonnte.ui.addMedicine

sealed class AddMedicineScreenState {

    object Loading : AddMedicineScreenState()
}

data class FormState(
    val name: String = "",
    val aisleNumber: String ="",
    val currentStock: String = "",
    val formErrors: FormErrorState = FormErrorState(),
)

data class FormErrorState(
    val nameError: Boolean = false,
    val nameLengthError: Boolean = false,
    val stockDigitError: Boolean = false,
    val aisleDigitError: Boolean = false,
)
sealed class SaveState {
    object Idle : SaveState()
    object Loading : SaveState()
    object MedicineSaved : SaveState()
    data class Error(val messageId: Int) : SaveState()
}