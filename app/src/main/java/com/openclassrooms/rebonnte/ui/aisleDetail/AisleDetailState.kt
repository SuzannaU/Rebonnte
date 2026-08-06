package com.openclassrooms.rebonnte.ui.aisleDetail

import com.openclassrooms.rebonnte.ui.model.MedicineUi

sealed class AisleDetailScreenState {
    object Loading : AisleDetailScreenState()
    object AisleNotFound : AisleDetailScreenState()

    data class AisleFound(
        val aisleNumber: String,
        val medicines: List<MedicineUi>,
    ) : AisleDetailScreenState()

    data class Error(
        val errorMessageId: Int,
    ) : AisleDetailScreenState()
}