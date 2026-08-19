package com.openclassrooms.rebonnte.ui.addMedicine

import com.openclassrooms.rebonnte.ui.util.generateRandomAlphanumeric

data class FormState(
    val id: String = "",
    val name: String = "",
    val aisleNumber: String,
    val currentStock: String = "",
    val formErrors: FormErrorState = FormErrorState(),
)

data class FormErrorState(
    val nameError: Boolean = false,
    val nameLengthError: Boolean = false,
    val stockDigitError: Boolean = false,
    val aisleDigitError: Boolean = false,
    val aisleDoesNotExistError: Boolean = false,
    val aisleVerificationError: Boolean = false,
)
sealed class SaveState {
    object Idle : SaveState()
    object Loading : SaveState()
    object MedicineSaved : SaveState()
    data class Error(val messageId: Int) : SaveState()
}