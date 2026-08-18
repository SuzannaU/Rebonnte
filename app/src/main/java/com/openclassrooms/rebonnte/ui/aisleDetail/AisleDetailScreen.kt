package com.openclassrooms.rebonnte.ui.aisleDetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.ui.ErrorScreen
import com.openclassrooms.rebonnte.ui.LoadingScreen
import com.openclassrooms.rebonnte.ui.components.MedicineItem
import com.openclassrooms.rebonnte.ui.model.MedicineUi
import com.openclassrooms.rebonnte.ui.theme.RebonnteTheme

@Composable
fun AisleDetailScreen(
    viewModel: AisleDetailViewModel,
    onMedicineClick: (String) -> Unit,
    onAddMedicineClick: (String) -> Unit,
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is AisleDetailScreenState.AisleFound -> {
            AisleDetailContent(
                state.aisleNumber,
                state.medicines,
                onMedicineClick,
                onAddMedicineClick,
                onBackClick,
            )
        }

        is AisleDetailScreenState.Error -> {
            ErrorScreen(
                errorMessage = state.errorMessageId,
                isRetryEnabled = false,
                onRetry = {},
            )
        }

        AisleDetailScreenState.Loading -> {
            LoadingScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AisleDetailContent(
    aisleNumber: String,
    medicines: List<MedicineUi>,
    onMedicineClick: (String) -> Unit,
    onAddMedicineClick: (String) -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.aisle_number_n, aisleNumber),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.semantics { heading() }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back)
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddMedicineClick(aisleNumber) }
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_medicine))
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(medicines) { medicine ->
                    MedicineItem(
                        medicine = medicine,
                        showAisleNumber = false,
                        onClick = { onMedicineClick(medicine.id) },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AisleDetailContentPreview() {
    RebonnteTheme {
        AisleDetailContent(
            aisleNumber = "15",
            medicines = listOf(
                MedicineUi("1", "Paracetamol", "1", "10"),
                MedicineUi("2", "Ibuprofen", "1", "5"),
                MedicineUi("3", "Aspirin", "1", "20")
            ),
            onMedicineClick = {},
            onAddMedicineClick = {},
            onBackClick = {},
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun AisleDetailContentDarkPreview() {
    RebonnteTheme {
        AisleDetailContent(
            aisleNumber = "15",
            medicines = listOf(
                MedicineUi("1", "Paracetamol", "1", "10"),
                MedicineUi("2", "Ibuprofen", "1", "5"),
                MedicineUi("3", "Aspirin", "1", "20")
            ),
            onMedicineClick = {},
            onAddMedicineClick = {},
            onBackClick = {},
        )
    }
}