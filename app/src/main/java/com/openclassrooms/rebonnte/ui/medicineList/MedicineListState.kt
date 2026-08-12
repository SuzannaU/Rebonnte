package com.openclassrooms.rebonnte.ui.medicineList

import com.openclassrooms.rebonnte.ui.model.MedicineUi

sealed class MedicineListScreenState {

    object Loading : MedicineListScreenState()
    data class MedicinesFound(
        val medicines : List<MedicineUi>
    ) : MedicineListScreenState()

    data class Error(val message: Int) : MedicineListScreenState()
}