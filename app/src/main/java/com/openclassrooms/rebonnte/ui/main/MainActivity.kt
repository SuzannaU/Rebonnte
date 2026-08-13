package com.openclassrooms.rebonnte.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.ui.ADD_MEDICINE_BASE_ROUTE
import com.openclassrooms.rebonnte.ui.ADD_MEDICINE_ROUTE
import com.openclassrooms.rebonnte.ui.AISLE_DETAIL_ROUTE
import com.openclassrooms.rebonnte.ui.AISLE_LIST_ROUTE
import com.openclassrooms.rebonnte.ui.ErrorScreen
import com.openclassrooms.rebonnte.ui.LoadingScreen
import com.openclassrooms.rebonnte.ui.MEDICINE_DETAIL_ROUTE
import com.openclassrooms.rebonnte.ui.MEDICINE_LIST_ROUTE
import com.openclassrooms.rebonnte.ui.addMedicine.AddMedicineScreen
import com.openclassrooms.rebonnte.ui.aisleDetail.AisleDetailScreen
import com.openclassrooms.rebonnte.ui.aisleList.AisleListScreen
import com.openclassrooms.rebonnte.ui.medicineDetail.MedicineDetailScreen
import com.openclassrooms.rebonnte.ui.medicineList.MedicineListScreen
import com.openclassrooms.rebonnte.ui.theme.RebonnteTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
        ::onSignInResult,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            RebonnteTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    when {
                        uiState.errorMessageId != null -> {
                            ErrorScreen(
                                errorMessage = uiState.errorMessageId!!,
                                isRetryEnabled = true,
                                onRetry = {
                                    startSignInActivity()
                                },
                            )
                        }

                        uiState.isLoading -> {
                            LoadingScreen()
                        }

                        !uiState.isAuthConnected -> {
                            ErrorScreen(
                                errorMessage = R.string.auth_connexion_problem,
                                isRetryEnabled = true,
                                onRetry = {
                                    startSignInActivity()
                                },
                            )
                        }

                        uiState.isUserAuthenticated -> {
                            key(uiState.userId) {
                                val navController = rememberNavController()
                                RebonnteNavHost(
                                    navController = navController,
                                )
                            }
                        }

                        else -> {
                            startSignInActivity()
                        }
                    }
                }
            }
        }
    }

    private fun startSignInActivity() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
        )

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setTheme(R.style.Theme_Rebonnte_Login)
            .setAvailableProviders(providers)
            //.setLogo(R.drawable.logo)
            .build()

        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            viewModel.createUser()
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