package com.openclassrooms.rebonnte.data.dto

import com.google.firebase.Timestamp
import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.model.User

fun User.toDto(): UserDto {
    return UserDto(
        id = this.id,
        username = this.username,
        email = this.email,
    )
}

fun UserDto.toDomain(): User {
    return User(
        id = this.id,
        username = this.username,
        email = this.email,
    )
}

fun Aisle.toDto(): AisleDto {
    return AisleDto(
        id = this.id,
        number = this.number,
    )
}

fun AisleDto.ToDomain(): Aisle {
    return Aisle(
        id = this.id,
        number = this.number,
    )
}

fun Medicine.toDto(): MedicineDto {
    return MedicineDto(
        id = this.id,
        name = this.name,
        aisleNumber = this.aisleNumber,
        currentStock = this.currentStock,
    )
}

fun MedicineDto.toDomain(): Medicine {
    return Medicine(
        id = this.id,
        name = this.name,
        aisleNumber = this.aisleNumber,
        currentStock = this.currentStock
    )
}

fun History.toDto(): HistoryDto {
    return HistoryDto(
        id = this.id,
        medicineId = this.medicineId,
        userId = this.userId,
        dateTime = Timestamp(this.dateTime),
        details = this.details,
    )
}

fun HistoryDto.toDomain() : History {
    return History(
        id = this.id,
        medicineId = this.medicineId,
        userId = this.userId,
        dateTime = this.dateTime.toDate(),
        details = this.details,
    )
}