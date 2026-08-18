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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.ui.ErrorScreen
import com.openclassrooms.rebonnte.ui.LoadingScreen
import com.openclassrooms.rebonnte.ui.theme.RebonnteTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicineScreen(
    viewModel: AddMedicineViewModel,
    onBackClick: () -> Unit,
    onMedicineSaved: () -> Unit,
) {
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val saveState by viewModel.saveState.collectAsStateWithLifecycle()

    LaunchedEffect(saveState) {
        if (saveState is SaveState.MedicineSaved) {
            onMedicineSaved()
        }
    }

    when (val state = saveState) {
        is SaveState.Idle -> {
            AddMedicineContent(
                formState = formState,
                onNameChange = viewModel::updateName,
                onAisleChange = viewModel::updateAisle,
                onStockChange = viewModel::updateStock,
                onAddClick = viewModel::onAddMedicine,
                onBackClick = onBackClick
            )
        }

        is SaveState.Error -> {
            ErrorScreen(
                errorMessage = state.messageId,
                isRetryEnabled = true,
                onRetry = viewModel::onAddMedicine,
            )
        }

        SaveState.Loading -> {
            LoadingScreen()
        }

        SaveState.MedicineSaved -> {}       // Handled by the LaunchEffect
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddMedicineContent(
    formState: FormState,
    onNameChange: (String) -> Unit,
    onAisleChange: (String) -> Unit,
    onStockChange: (String) -> Unit,
    onAddClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.add_medicine),
                        modifier = Modifier.semantics { heading() }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(
                                R.string.navigate_back
                            )
                        )
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
                        Text(
                            text = stringResource(R.string.error_name_empty),
                            modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                        )
                    } else if (formState.formErrors.nameLengthError) {
                        Text(
                            text = stringResource(R.string.error_name_too_long),
                            modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                        )
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
                isError = formState.formErrors.aisleDigitError || formState.formErrors.aisleDoesNotExistError || formState.formErrors.aisleVerificationError,
                supportingText = {
                    if (formState.formErrors.aisleDigitError) {
                        Text(
                            text = stringResource(R.string.error_aisle_invalid),
                            modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                        )
                    } else if (formState.formErrors.aisleDoesNotExistError) {
                        Text(
                            text = stringResource(R.string.error_aisle_not_exists),
                            modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                        )
                    } else if (formState.formErrors.aisleVerificationError) {
                        Text(
                            text = stringResource(R.string.error_aisle_unverifiable),
                            modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
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
                        Text(
                            text = stringResource(R.string.error_stock_invalid),
                            modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onAddClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.add_medicine))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddMedicineContentPreview() {
    RebonnteTheme {
        AddMedicineContent(
            formState = FormState(aisleNumber = "10"),
            onNameChange = {},
            onAisleChange = {},
            onStockChange = {},
            onAddClick = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun AddMedicineContentDarkPreview() {
    RebonnteTheme {
        AddMedicineContent(
            formState = FormState(aisleNumber = "10"),
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
            onNameChange = {},
            onAisleChange = {},
            onStockChange = {},
            onAddClick = {},
            onBackClick = {}
        )
    }
}