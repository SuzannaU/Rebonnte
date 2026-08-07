package com.openclassrooms.rebonnte.ui.model

import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.model.UpdatableFields
import com.openclassrooms.rebonnte.domain.model.User
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.ui.util.UiText
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
            isCreation -> UiText.StringResource(R.string.history_creation)
            updatedField == null ->
                UiText.StringResource(R.string.history_no_details)
            updatedField.field == UpdatableFields.AISLE ->
                UiText.StringResource(
                    R.string.history_update_format,
                    UiText.StringResource(R.string.aisle_number),
                    updatedField.oldValue,
                    updatedField.newValue
                )
            updatedField.field == UpdatableFields.NAME ->
                UiText.StringResource(
                    R.string.history_update_format,
                    UiText.StringResource(R.string.name),
                    updatedField.oldValue,
                    updatedField.newValue
                )
            updatedField.field == UpdatableFields.STOCK ->
                UiText.StringResource(
                    R.string.history_update_format,
                    UiText.StringResource(R.string.stock),
                    updatedField.oldValue,
                    updatedField.newValue
                )
            else -> UiText.StringResource(R.string.history_no_details)
        }
    )
}

fun Date.formatToString(): String {
    val locale = Locale.getDefault()

    val formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale)
    return formatter.format(this)
}