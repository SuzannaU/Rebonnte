package com.openclassrooms.rebonnte.ui.addMedicine

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openclassrooms.rebonnte.R

import androidx.compose.ui.tooling.preview.Preview
import com.openclassrooms.rebonnte.ui.theme.RebonnteTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicineScreen(
    viewModel: AddMedicineViewModel,
    onBackClick: () -> Unit,
) {
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val saveState by viewModel.saveState.collectAsStateWithLifecycle()

    LaunchedEffect(saveState) {
        if (saveState is SaveState.MedicineSaved) {
            onBackClick()
        }
    }

    AddMedicineContent(
        formState = formState,
        saveState = saveState,
        onNameChange = viewModel::updateName,
        onAisleChange = viewModel::updateAisle,
        onStockChange = viewModel::updateStock,
        onAddClick = viewModel::addMedicine,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddMedicineContent(
    formState: FormState,
    saveState: SaveState,
    onNameChange: (String) -> Unit,
    onAisleChange: (String) -> Unit,
    onStockChange: (String) -> Unit,
    onAddClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_medicine)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = formState.name,
                onValueChange = onNameChange,
                label = { Text(stringResource(R.string.medicine_name)) },
                isError = formState.formErrors.nameError || formState.formErrors.nameLengthError,
                supportingText = {
                    if (formState.formErrors.nameError) {
                        Text(stringResource(R.string.error_name_empty))
                    } else if (formState.formErrors.nameLengthError) {
                        Text(stringResource(R.string.error_name_too_long))
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = formState.aisleNumber,
                onValueChange = onAisleChange,
                label = { Text(stringResource(R.string.aisle_number)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = formState.formErrors.aisleDigitError,
                supportingText = {
                    if (formState.formErrors.aisleDigitError) {
                        Text(stringResource(R.string.error_aisle_invalid))
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = formState.currentStock,
                onValueChange = onStockChange,
                label = { Text(stringResource(R.string.current_stock)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = formState.formErrors.stockDigitError,
                supportingText = {
                    if (formState.formErrors.stockDigitError) {
                        Text(stringResource(R.string.error_stock_invalid))
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (saveState is SaveState.Loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = onAddClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.add_medicine))
                }
            }

            if (saveState is SaveState.Error) {
                Text(
                    text = stringResource(saveState.messageId),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddMedicineContentPreview() {
    RebonnteTheme {
        AddMedicineContent(
            formState = FormState(),
            saveState = SaveState.Idle,
            onNameChange = {},
            onAisleChange = {},
            onStockChange = {},
            onAddClick = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddMedicineErrorPreview() {
    RebonnteTheme {
        AddMedicineContent(
            formState = FormState(
                name = "",
                aisleNumber = "abc",
                currentStock = "-1",
                formErrors = FormErrorState(
                    nameError = true,
                    aisleDigitError = true,
                    stockDigitError = true
                )
            ),
            saveState = SaveState.Idle,
            onNameChange = {},
            onAisleChange = {},
            onStockChange = {},
            onAddClick = {},
            onBackClick = {}
        )
    }
}