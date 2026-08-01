package com.openclassrooms.rebonnte.ui.medicineDetail

import com.openclassrooms.rebonnte.ui.model.HistoryUi
import com.openclassrooms.rebonnte.ui.model.MedicineUi

sealed class MedicineDetailState {

    object Loading : MedicineDetailState()
    object MedicineNotFound : MedicineDetailState()

    data class MedicineFound(
        val medicine: MedicineUi,
        val histories: List<HistoryUi>
    ) : MedicineDetailState()
}