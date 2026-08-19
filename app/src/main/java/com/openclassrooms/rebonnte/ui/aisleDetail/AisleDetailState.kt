package com.openclassrooms.rebonnte.ui.aisleDetail

import androidx.compose.runtime.Immutable
import com.openclassrooms.rebonnte.ui.model.MedicineUi

sealed class AisleDetailScreenState {
    object Loading : AisleDetailScreenState()

    @Immutable
    data class AisleFound(
        val aisleNumber: String,
        val medicines: List<MedicineUi>,
    ) : AisleDetailScreenState()

    data class Error(
        val errorMessageId: Int,
    ) : AisleDetailScreenState()
}