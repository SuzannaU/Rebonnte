package com.openclassrooms.rebonnte.ui.addMedicine

data class FormState(
    val id: String = generateRandomAlphanumeric(20),
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
)
sealed class SaveState {
    object Idle : SaveState()
    object Loading : SaveState()
    object MedicineSaved : SaveState()
    data class Error(val messageId: Int) : SaveState()
}

fun generateRandomAlphanumeric(length: Int): String {
    val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    return List(length) { charPool.random() }
        .joinToString("")
}