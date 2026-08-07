package com.openclassrooms.rebonnte.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
                onAisleClick = { number -> navController.navigate("aisleDetail/$number") },
                onMedicinesClick = { navController.navigate(MEDICINE_LIST_ROUTE) }
            )
        }
        composable(
            AISLE_DETAIL_ROUTE,
            arguments = listOf(
                navArgument("aisleNumber") {
                    type = NavType.StringType
                }
            )
        ) {
            AisleDetailScreen(
                viewModel = koinViewModel(),
                onMedicineClick = { id -> navController.navigate("medicineDetail/$id") },
                onAddMedicineClick = { aisleNumber -> navController.navigate("addMedicine?aisleNumber=$aisleNumber") },
                onBackClick = { navController.navigateUp() }
            )
        }
        composable(MEDICINE_LIST_ROUTE) {
            MedicineListScreen(
                viewModel = koinViewModel(),
                onMedicineClick = { id -> navController.navigate("medicineDetail/$id") },
                onAislesClick = { navController.navigate(AISLE_LIST_ROUTE) },
                onAddMedicineClick = { navController.navigate(ADD_MEDICINE_BASE_ROUTE) }
            )
        }
        composable(
            MEDICINE_DETAIL_ROUTE,
            arguments = listOf(
                navArgument("medicineId") {
                    type = NavType.StringType
                }
            )
        ) {
            MedicineDetailScreen(
                viewModel = koinViewModel(),
                onBackClick = { navController.navigateUp() },
            )
        }
        composable(
            ADD_MEDICINE_ROUTE,
            arguments = listOf(
                navArgument("aisleNumber") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = ""
                }
            )
        ) {
            AddMedicineScreen(
                viewModel = koinViewModel(),
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}