package com.openclassrooms.rebonnte.ui.medicineDetail

import com.openclassrooms.rebonnte.ui.model.HistoryUi
import com.openclassrooms.rebonnte.ui.model.MedicineUi

sealed class MedicineDetailState {

    object Loading : MedicineDetailState()
    object MedicineNotFound : MedicineDetailState()

    data class MedicineFound(
        val medicine: MedicineUi,
        val histories: List<HistoryUi>,
    ) : MedicineDetailState()

    data class Error(val messageId: Int) : MedicineDetailState()
}

data class EditMedicineFormState(
    val name: String = "",
    val aisleNumber: String = "",
    val stock: String = "",
    val formError: EditMedicineFormErrorState = EditMedicineFormErrorState(),
    val isSuccess: Boolean = false,
    val errorId: Int? = null,
)

data class EditMedicineFormErrorState(
    val nameBlankError: Boolean = false,
    val nameLengthError: Boolean = false,
    val aisleBlankError: Boolean = false,
    val aisleDigitError: Boolean = false,
    val aisleDoesNotExistError: Boolean = false,
    val stockBlankError: Boolean = false,
    val stockDigitError: Boolean = false,
)