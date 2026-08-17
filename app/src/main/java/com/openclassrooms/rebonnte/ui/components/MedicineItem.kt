package com.openclassrooms.rebonnte.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.ui.model.MedicineUi
import com.openclassrooms.rebonnte.ui.theme.RebonnteTheme


@Composable
fun MedicineItem(
    medicine: MedicineUi,
    showAisleNumber: Boolean,
    onClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(medicine.id) }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = medicine.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            if (showAisleNumber) {
                Text(
                    text = stringResource(
                        R.string.aisle_n_stock_n,
                        medicine.aisleNumber,
                        medicine.currentStock,
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Text(
                    text = stringResource(R.string.stock_n, medicine.currentStock),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = stringResource(
                R.string.navigate_to_medicine_name, medicine.name
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MedicineItemPreview() {
    RebonnteTheme {
        MedicineItem(
            medicine = MedicineUi("1", "Paracetamol", "1", "10"),
            onClick = {},
            showAisleNumber = false
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MedicineItemWithAislePreview() {
    RebonnteTheme {
        MedicineItem(
            medicine = MedicineUi("1", "Paracetamol", "1", "10"),
            onClick = {},
            showAisleNumber = true,
        )
    }
}