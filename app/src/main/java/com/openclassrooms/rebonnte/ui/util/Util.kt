package com.openclassrooms.rebonnte.ui.util



fun generateRandomAlphanumeric(length: Int): String {
    val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    return List(length) { charPool.random() }
        .joinToString("")
}