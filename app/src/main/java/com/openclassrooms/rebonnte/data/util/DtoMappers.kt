package com.openclassrooms.rebonnte.data.util

import com.google.firebase.Timestamp
import com.openclassrooms.rebonnte.data.dto.AisleDto
import com.openclassrooms.rebonnte.data.dto.HistoryDto
import com.openclassrooms.rebonnte.data.dto.MedicineDto
import com.openclassrooms.rebonnte.data.dto.UpdatedFieldDto
import com.openclassrooms.rebonnte.data.dto.UserDto
import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.model.UpdatedField
import com.openclassrooms.rebonnte.domain.model.User

fun User.toDto(): UserDto {
    return UserDto(
        id = id,
        username = username,
        email = email,
    )
}

fun UserDto.toDomain(): User {
    return User(
        id = id,
        username = username,
        email = email,
    )
}

fun Aisle.toDto(): AisleDto {
    return AisleDto(
        id = number,
    )
}

fun AisleDto.toDomain(): Aisle {
    return Aisle(
        number = id,
    )
}

fun Medicine.toDto(): MedicineDto {
    return MedicineDto(
        id = id,
        name = name,
        aisleNumber = aisleNumber,
        currentStock = currentStock,
        archived = isArchived,
    )
}

fun MedicineDto.toDomain(): Medicine {
    return Medicine(
        id = id,
        name = name,
        aisleNumber = aisleNumber,
        currentStock = currentStock,
        isArchived = archived,
    )
}

fun History.toDto(): HistoryDto {
    return HistoryDto(
        id = id,
        medicineId = medicineId,
        userId = userId,
        dateTime = Timestamp(dateTime),
        creation = isCreation,
        archiving = isArchiving,
        updatedField = updatedField?.toDto(),
    )
}

fun HistoryDto.toDomain(): History {
    return History(
        id = id,
        medicineId = medicineId,
        userId = userId,
        dateTime = dateTime.toDate(),
        isCreation = creation,
        isArchiving = archiving,
        updatedField = updatedField?.toDomain(),
    )
}

fun UpdatedField.toDto() = UpdatedFieldDto(
    field = field,
    oldValue = oldValue,
    newValue = newValue
)

fun UpdatedFieldDto.toDomain() = UpdatedField(
    field = field,
    oldValue = oldValue,
    newValue = newValue
)