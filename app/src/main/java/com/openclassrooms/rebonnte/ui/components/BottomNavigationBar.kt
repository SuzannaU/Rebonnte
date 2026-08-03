package com.openclassrooms.rebonnte.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.openclassrooms.rebonnte.ui.AISLE_LIST_ROUTE
import com.openclassrooms.rebonnte.ui.MEDICINE_LIST_ROUTE

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onAislesClick: () -> Unit,
    onMedicinesClick: () -> Unit,
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Aisles") },
            selected = currentRoute == AISLE_LIST_ROUTE,
            onClick = onAislesClick,
        )
        NavigationBarItem(
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
            label = { Text("Medicines") },
            selected = currentRoute == MEDICINE_LIST_ROUTE,
            onClick = onMedicinesClick,
        )
    }
}