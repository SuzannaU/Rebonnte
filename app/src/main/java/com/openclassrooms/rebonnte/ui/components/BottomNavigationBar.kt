package com.openclassrooms.rebonnte.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.ui.AISLE_LIST_ROUTE
import com.openclassrooms.rebonnte.ui.MEDICINE_LIST_ROUTE
import com.openclassrooms.rebonnte.ui.theme.RebonnteTheme

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onAislesClick: () -> Unit,
    onMedicinesClick: () -> Unit,
) {
    NavigationBar {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = if (currentRoute == MEDICINE_LIST_ROUTE) stringResource(R.string.navigate_to_aisles_list) else null,
                )
            },
            label = { Text(stringResource(R.string.aisles)) },
            selected = currentRoute == AISLE_LIST_ROUTE,
            onClick = onAislesClick,
        )
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.AutoMirrored.Filled.List,
                    contentDescription = if (currentRoute == AISLE_LIST_ROUTE) stringResource(R.string.navigate_to_medicines_list) else null,
                )
            },
            label = { Text(stringResource(R.string.medicines)) },
            selected = currentRoute == MEDICINE_LIST_ROUTE,
            onClick = onMedicinesClick,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomNavigationBarPreview() {
    RebonnteTheme {
        BottomNavigationBar(
            currentRoute = AISLE_LIST_ROUTE,
            onAislesClick = {},
            onMedicinesClick = {},
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun BottomNavigationBarDarkPreview() {
    RebonnteTheme {
        BottomNavigationBar(
            currentRoute = AISLE_LIST_ROUTE,
            onAislesClick = {},
            onMedicinesClick = {},
        )
    }
}