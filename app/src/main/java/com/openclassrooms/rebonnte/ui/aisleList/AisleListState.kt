package com.openclassrooms.rebonnte.ui.aisleList

import com.openclassrooms.rebonnte.ui.model.AisleUi

sealed class AisleListScreenState {
    object Loading : AisleListScreenState()
    object NoAisleFound : AisleListScreenState()

    data class AislesFound(
        val aisles : List<AisleUi>
    ) : AisleListScreenState()

    data class Error(
        val errorMessageId: Int,
    ) : AisleListScreenState()
}

data class AddAisleFormState(
    val aisleBlankError: Boolean = false,
    val aisleDigitError: Boolean = false,
    val aisleExistsError: Boolean = false,
    val isSuccess: Boolean = false,
)