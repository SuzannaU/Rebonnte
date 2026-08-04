package com.openclassrooms.rebonnte.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.openclassrooms.rebonnte.ui.addMedicine.AddMedicineScreen
import com.openclassrooms.rebonnte.ui.aisleDetail.AisleDetailScreen
import com.openclassrooms.rebonnte.ui.aisleList.AisleListScreen
import com.openclassrooms.rebonnte.ui.medicineDetail.MedicineDetailScreen
import com.openclassrooms.rebonnte.ui.medicineList.MedicineListScreen
import com.openclassrooms.rebonnte.ui.theme.RebonnteTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RebonnteTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    RebonnteNavHost(
                        navController = navController,
                    )
                }
            }
        }
    }
}

@Composable
private fun RebonnteNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = AISLE_LIST_ROUTE
    ) {
        composable(AISLE_LIST_ROUTE) {
            AisleListScreen(
                viewModel = koinViewModel(),
                onAisleClick = { id -> navController.navigate("aisleDetail/$id") },
                onMedicinesClick = { navController.navigate(MEDICINE_LIST_ROUTE) }
            )
        }
        composable(
            AISLE_DETAIL_ROUTE,
            arguments = listOf(navArgument("aisleNumber") { type = NavType.StringType })
        ) { backStackEntry ->
            val aisleNumber = backStackEntry.arguments?.getString("aisleNumber") ?: ""
            AisleDetailScreen(
                viewModel = koinViewModel { parametersOf(aisleNumber) },
                onMedicineClick = { number -> navController.navigate("medicineDetail/$number") },
                onBackClick = { navController.navigateUp() }
            )
        }
        composable(MEDICINE_LIST_ROUTE) {
            MedicineListScreen(
                viewModel = koinViewModel(),
                onMedicineClick = { id -> navController.navigate("medicineDetail/$id") },
                onAislesClick = { navController.navigate(AISLE_LIST_ROUTE) },
                onAddMedicineClick = { navController.navigate(ADD_MEDICINE_ROUTE) }
            )
        }
        composable(
            MEDICINE_DETAIL_ROUTE,
            arguments = listOf(navArgument("medicineId") { type = NavType.StringType })
        ) { backStackEntry ->
            val medicineId = backStackEntry.arguments?.getString("medicineId") ?: ""
            MedicineDetailScreen(
                viewModel = koinViewModel { parametersOf(medicineId) },
                onBackClick = { navController.navigateUp() },
            )
        }
        composable(ADD_MEDICINE_ROUTE) {
            AddMedicineScreen(
                viewModel = koinViewModel(),
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}