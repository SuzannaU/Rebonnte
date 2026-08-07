package com.openclassrooms.rebonnte.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.openclassrooms.rebonnte.R

@Composable
fun ConfirmationDialog(
    title: String,
    text: String,
    onDismissRequest: () -> Unit,
    onDismissClick: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(Icons.Filled.Info, contentDescription = null)
        },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = text)
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onConfirmClick
            ) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissClick
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}