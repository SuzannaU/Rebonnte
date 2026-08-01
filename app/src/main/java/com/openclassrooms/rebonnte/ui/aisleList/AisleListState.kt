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