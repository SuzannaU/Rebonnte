package com.openclassrooms.rebonnte.ui.aisleDetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.tooling.preview.Preview
import com.openclassrooms.rebonnte.ui.LoadingScreen
import com.openclassrooms.rebonnte.ui.theme.RebonnteTheme
import com.openclassrooms.rebonnte.ui.model.MedicineUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AisleDetailScreen(
    viewModel: AisleDetailViewModel,
    onMedicineClick: (String) -> Unit,
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    //val aisle = (uiState as? AisleDetailScreenState.AisleFound)?.aisle
                    Text(
//                        text = aisle?.let {
//                            "Aisle # ${it.number}"
//                        } ?: "No Aisle Found",
                        text = "Aisle # ${viewModel.aisleNumber}",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "navigate back"
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is AisleDetailScreenState.AisleFound -> {
                    AisleDetailContent(state.medicines, onMedicineClick)
                }

                AisleDetailScreenState.AisleNotFound -> {}
                is AisleDetailScreenState.Error -> {}
                AisleDetailScreenState.Loading -> {
                    LoadingScreen()
                }
            }
        }
    }
}

@Composable
private fun AisleDetailContent(
    medicines: List<MedicineUi>,
    onMedicineClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(medicines) { medicine ->
            MedicineItem(
                medicine = medicine,
                onClick = { onMedicineClick(medicine.id) }
            )
        }
    }
}

@Composable
private fun MedicineItem(
    medicine: MedicineUi,
    onClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(medicine.id) }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = medicine.name, fontWeight = FontWeight.Bold)
            Text(text = "Stock: ${medicine.currentStock}", color = Color.Gray)
        }
        Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Arrow")
    }
}

@Preview(showBackground = true)
@Composable
private fun AisleDetailContentPreview() {
    RebonnteTheme {
        AisleDetailContent(
            medicines = listOf(
                MedicineUi("1", "Paracetamol", "1", "10"),
                MedicineUi("2", "Ibuprofen", "1", "5"),
                MedicineUi("3", "Aspirin", "1", "20")
            ),
            onMedicineClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MedicineItemPreview() {
    RebonnteTheme {
        MedicineItem(
            medicine = MedicineUi("1", "Paracetamol", "1", "10"),
            onClick = {}
        )
    }
}