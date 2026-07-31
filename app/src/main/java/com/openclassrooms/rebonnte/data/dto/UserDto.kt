package com.openclassrooms.rebonnte.data.dto

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.io.Serializable

data class UserDto(
    @DocumentId
    val id: String = "",
    @PropertyName("username")
    val username: String = "",
    val email: String = "",
) : Serializable
