package com.openclassrooms.rebonnte.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.ui.theme.RebonnteTheme

@Composable
fun TextFieldDialog(
    title: String,
    label: String,
    initialValue: String,
    isDigits: Boolean = false,
    isError: Boolean = false,
    errorText: String? = null,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        TextFieldDialogContent(
            title = title,
            label = label,
            initialValue = initialValue,
            isDigits = isDigits,
            isError = isError,
            errorText = errorText,
            onDismiss = onDismiss,
            onConfirm = onConfirm
        )
    }
}

@Composable
private fun TextFieldDialogContent(
    title: String,
    label: String,
    initialValue: String,
    isDigits: Boolean = false,
    isError: Boolean = false,
    errorText: String? = null,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var value by rememberSaveable { mutableStateOf(initialValue) }

    Surface(
        shape = RoundedCornerShape(28.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(28.dp),
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = value,
                onValueChange = { input -> value = input },
                label = { Text(label) },
                isError = isError,
                supportingText = {
                    if (errorText != null) {
                        Text(
                            text = errorText,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                        )
                    }
                },
                keyboardOptions = if (isDigits) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onDismiss
                ) {
                    Text(stringResource(R.string.dismiss))
                }
                Button(
                    onClick = { onConfirm(value) }
                ) {
                    Text(stringResource(R.string.confirm))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddAisleDialogPreview() {
    RebonnteTheme {
        TextFieldDialogContent(
            onDismiss = {},
            onConfirm = {},
            title = "Title",
            label = "Label",
            initialValue = "Initial Value",
            isDigits = false,
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun AddAisleDialogDarkPreview() {
    RebonnteTheme {
        TextFieldDialogContent(
            onDismiss = {},
            onConfirm = {},
            title = "Title",
            label = "Label",
            initialValue = "Initial Value",
            isDigits = false,
        )
    }
}