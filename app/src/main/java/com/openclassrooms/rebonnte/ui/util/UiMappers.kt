package com.openclassrooms.rebonnte.ui.util

import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.model.MedicineSortOption
import com.openclassrooms.rebonnte.domain.model.UpdatableFields
import com.openclassrooms.rebonnte.ui.model.AisleUi
import com.openclassrooms.rebonnte.ui.model.HistoryUi
import com.openclassrooms.rebonnte.ui.model.MedicineUi
import com.openclassrooms.rebonnte.ui.model.SortOption
import java.text.DateFormat
import java.util.Date
import java.util.Locale

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

fun History.toUi(username: UiText): HistoryUi {
    return HistoryUi(
        medicineId = this.medicineId,
        username = username,
        dateTime = this.dateTime.formatToString(),
        details = when {
            isCreation -> UiText.StringResource(R.string.creation)
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

fun SortOption.toDomainSortOption(): MedicineSortOption {
    return when (this) {
        SortOption.NAME_ASCENDING -> MedicineSortOption.NAME_ASCENDING
        SortOption.NAME_DESCENDING -> MedicineSortOption.NAME_DESCENDING
        SortOption.STOCK_ASCENDING -> MedicineSortOption.STOCK_ASCENDING
        SortOption.STOCK_DESCENDING -> MedicineSortOption.STOCK_DESCENDING
    }
}

fun Date.formatToString(): String {
    val locale = Locale.getDefault()

    val formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale)
    return formatter.format(this)
}