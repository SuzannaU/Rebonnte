package com.openclassrooms.rebonnte.ui.model

import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.model.Medicine
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

fun UserUi.toDomain(): User {
    return User(
        id = "",                    // TODO figure out how to deal with ids
        username = this.username,
        email = this.email,
    )
}

fun Aisle.toUi(): AisleUi {
    return AisleUi(
        id = this.id,
        number = this.number,
    )
}

fun AisleUi.toDomain(): Aisle {
    return Aisle(
        id = this.id,
        number = this.number,
    )
}

fun Medicine.toUi(): MedicineUi {
    return MedicineUi(
        id = this.id,
        name = this.name,
        currentStock = this.currentStock,
    )
}

fun MedicineUi.toMedicine(): Medicine {
    return Medicine(
        id = this.id,
        name = this.name,
        currentStock = this.currentStock,
        aisleId = "",
    )
}

fun History.toUi(): HistoryUi {
    return HistoryUi(
        medicineId = this.medicineId,
        userId = this.userId,
        dateTime = this.dateTime.formatToString(),
        details = this.details,
    )
}

// TODO : do I need HistoryUi.toDomain?

fun Date.formatToString(): String {
    val locale = Locale.getDefault()

    val formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale)
    return formatter.format(this)
}