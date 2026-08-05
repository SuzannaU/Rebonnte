package com.openclassrooms.rebonnte.ui.model

import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.model.UpdatableFields
import com.openclassrooms.rebonnte.domain.model.User
import java.text.DateFormat
import java.util.Date
import java.util.Locale

fun User.toUi(): UserUi {
    return UserUi(
        username = this.username,
        email = this.email,
    )
}

fun Aisle.toUi(): AisleUi {
    return AisleUi(
        number = this.number,
    )
}

fun Medicine.toUi(): MedicineUi {
    return MedicineUi(
        id = this.id,
        name = this.name,
        aisleNumber = this.aisleNumber,
        currentStock = this.currentStock.toString(),
    )
}

fun History.toUi(): HistoryUi {
    return HistoryUi(
        medicineId = this.medicineId,
        userId = this.userId,
        dateTime = this.dateTime.formatToString(),
        details = when {
            isCreation -> "Creation"
            updatedField == null && !isCreation ->
                "No details available1"
            updatedField?.field == UpdatableFields.AISLE ->
                "Aisle number changed from ${updatedField.oldValue} to ${updatedField.newValue}"
            updatedField?.field == UpdatableFields.NAME ->
                "Name changed from ${updatedField.oldValue} to ${updatedField.newValue}"
            updatedField?.field == UpdatableFields.STOCK ->
                "Stock changed from ${updatedField.oldValue} to ${updatedField.newValue}"
            else -> "No details available2"
        }
    )
}

fun Date.formatToString(): String {
    val locale = Locale.getDefault()

    val formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale)
    return formatter.format(this)
}