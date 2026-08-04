package com.openclassrooms.rebonnte.ui.medicineDetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openclassrooms.rebonnte.ui.LoadingScreen
import com.openclassrooms.rebonnte.ui.model.HistoryUi
import com.openclassrooms.rebonnte.ui.model.MedicineUi
import com.openclassrooms.rebonnte.ui.theme.RebonnteTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineDetailScreen(
    viewModel: MedicineDetailViewModel,
    onBackClick: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    val medicine = (uiState as? MedicineDetailState.MedicineFound)?.medicine
                    Text(
                        text = medicine?.let {
                            it.name
                        } ?: "No Medicine Found",
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
                MedicineDetailState.Loading -> {
                    LoadingScreen()
                }
                is MedicineDetailState.MedicineFound -> {
                    MedicineDetailContent(
                        medicine = state.medicine,
                        histories = state.histories,
                    )
                }
                MedicineDetailState.MedicineNotFound -> {}
            }
        }
    }
}

@Composable
private fun MedicineDetailContent(
    medicine: MedicineUi,
    histories: List<HistoryUi>,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        TextField(
            value = medicine.name,
            onValueChange = {},
            label = { Text("Name") },
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
//        TextField(
//            value = medicine.aisleId,
//            onValueChange = {},
//            label = { Text("Aisle") },
//            enabled = false,
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = {
                //if (medicine.currentStock > 0) {
//                        medicine.histories.toMutableList().add(        // IndexOutOfBoundsException
//                            History(
//                                medicine.name,
//                                "efeza56f1e65f",
//                                Date().toString(),
//                                "Updated medicine details"
//                            )
//                        )
//                        stock--
               // }
            }) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Minus One"
                )
            }
            TextField(
                value = medicine.currentStock,
                onValueChange = {},
                label = { Text("Stock") },
                enabled = false,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = {
//                    medicine.histories.toMutableList().add(        // IndexOutOfBoundsException
//                        History(
//                            medicine.name,
//                            "efeza56f1e65f",
//                            Date().toString(),
//                            "Updated medicine details"
//                        )
//                    )
//                    stock++
            }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Plus One"
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "History", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(histories) { history ->
                HistoryItem(history = history)
            }
        }
    }
}

@Composable
private fun HistoryItem(history: HistoryUi) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            //Text(text = history.medicineName, fontWeight = FontWeight.Bold)
            Text(text = "User: ${history.userId}")
            Text(text = "Date: ${history.dateTime}")
            Text(text = "Details: ${history.details}")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MedicineDetailContentPreview() {
    RebonnteTheme {
        MedicineDetailContent(
            medicine = MedicineUi("1", "Paracetamol", "1", "10"),
            histories = listOf(
                HistoryUi("1", "user1", "2026-08-01 10:00", "Initial stock"),
                HistoryUi("1", "user2", "2026-08-02 11:00", "Added 5 items")
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryItemPreview() {
    RebonnteTheme {
        HistoryItem(
            history = HistoryUi("1", "user1", "2026-08-01 10:00", "Initial stock")
        )
    }
}
