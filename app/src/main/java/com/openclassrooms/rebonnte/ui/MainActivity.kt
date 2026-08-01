package com.openclassrooms.rebonnte.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.openclassrooms.rebonnte.ui.aisleDetail.AisleDetailScreen
import com.openclassrooms.rebonnte.ui.aisleDetail.AisleDetailViewModel
import com.openclassrooms.rebonnte.ui.aisleList.AisleListScreen
import com.openclassrooms.rebonnte.ui.aisleList.AisleListViewModel
import com.openclassrooms.rebonnte.ui.medicineDetail.MedicineDetailScreen
import com.openclassrooms.rebonnte.ui.medicineList.MedicineListScreen
import com.openclassrooms.rebonnte.ui.medicineList.MedicineListViewModel
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
            //MyApp()
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
            arguments = listOf(navArgument("aisleId") { type = NavType.StringType })
        ) {
            AisleDetailScreen(
                viewModel = koinViewModel(),
                onMedicineClick = { id -> navController.navigate("medicineDetail/$id") },
                onBackClick = { navController.navigateUp() }
            )
        }
        composable(MEDICINE_LIST_ROUTE) {
            MedicineListScreen(
                viewModel = koinViewModel(),
                onMedicineClick = { id -> navController.navigate("medicineDetail/$id") },
                onAislesClick = { navController.navigate(AISLE_LIST_ROUTE) }
            )
        }
        composable(
            MEDICINE_DETAIL_ROUTE,
            arguments = listOf(navArgument("medicineId") { type = NavType.StringType })
        ) {
            MedicineDetailScreen(
                viewModel = koinViewModel(),
                onBackClick = { navController.navigateUp() },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
    val navController = rememberNavController()
    val medicineListViewModel: MedicineListViewModel = viewModel()
    val aisleListViewModel: AisleListViewModel = viewModel()
    val aisleDetailViewModel: AisleDetailViewModel = viewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val route = navBackStackEntry?.destination?.route

    RebonnteTheme {
        Scaffold(
            topBar = {
                var isSearchActive by rememberSaveable { mutableStateOf(false) }
                var searchQuery by remember { mutableStateOf("") }

                Column(verticalArrangement = Arrangement.spacedBy((-1).dp)) {
                    TopAppBar(
                        title = { if (route == AISLE_LIST_ROUTE) Text(text = "Aisle") else Text(text = "Medicines") },
//                        actions = {
//                            var expanded by remember { mutableStateOf(false) }
//                            if (currentRoute(navController) == MEDICINE_LIST_ROUTE) {
//                                Row(
//                                    verticalAlignment = Alignment.CenterVertically,
//                                    modifier = Modifier
//                                        .padding(end = 8.dp)
//                                        .background(MaterialTheme.colorScheme.surface)
//                                        .padding(horizontal = 8.dp, vertical = 4.dp)
//                                ) {
//                                    Box {
//                                        IconButton(onClick = { expanded = true }) {
//                                            Icon(Icons.Default.MoreVert, contentDescription = null)
//                                        }
//                                        DropdownMenu(
//                                            expanded = expanded,
//                                            onDismissRequest = { expanded = false },
//                                            offset = DpOffset(x = 0.dp, y = 0.dp)
//                                        ) {
//                                            DropdownMenuItem(
//                                                onClick = {
//                                                    medicineListViewModel.sortByNone()
//                                                    expanded = false
//                                                },
//                                                text = { Text("Sort by None") }
//                                            )
//                                            DropdownMenuItem(
//                                                onClick = {
//                                                    medicineListViewModel.sortByName()
//                                                    expanded = false
//                                                },
//                                                text = { Text("Sort by Name") }
//                                            )
//                                            DropdownMenuItem(
//                                                onClick = {
//                                                    medicineListViewModel.sortByStock()
//                                                    expanded = false
//                                                },
//                                                text = { Text("Sort by Stock") }
//                                            )
//                                        }
//                                    }
//                                }
//                            }
//                        }
                    )
//                    if (currentRoute(navController) == MEDICINE_LIST_ROUTE) {
//                        EmbeddedSearchBar(
//                            query = searchQuery,
//                            onQueryChange = {
//                                medicineListViewModel.filterByName(it)
//                                searchQuery = it
//                            },
//                            isSearchActive = isSearchActive,
//                            onActiveChanged = { isSearchActive = it }
//                        )
//                    }
                }
            },
//            bottomBar = {
//                NavigationBar {
//                    NavigationBarItem(
//                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
//                        label = { Text("Aisle") },
//                        selected = currentRoute(navController) == AISLE_LIST_ROUTE,
//                        onClick = { navController.navigate(AISLE_LIST_ROUTE) }
//                    )
//                    NavigationBarItem(
//                        icon = { Icon(Icons.Default.List, contentDescription = null) },
//                        label = { Text("Medicine") },
//                        selected = currentRoute(navController) == MEDICINE_LIST_ROUTE,
//                        onClick = { navController.navigate(MEDICINE_LIST_ROUTE) }
//                    )
//                }
//            },
//            floatingActionButton = {
//                FloatingActionButton(onClick = {
//                    if (route == MEDICINE_LIST_ROUTE) {
//                        medicineListViewModel.addRandomMedicine(aisleListViewModel.aisles.value)
//                    } else if (route == AISLE_LIST_ROUTE) {
//                        aisleListViewModel.addRandomAisle()
//                    }
//                }) {
//                    Icon(Icons.Default.Add, contentDescription = "Add")
//                }
//            }
        ) {
            NavHost(
                modifier = Modifier.padding(it),
                navController = navController,
                startDestination = AISLE_LIST_ROUTE
            ) {
                composable(AISLE_LIST_ROUTE) {
                    AisleListScreen(
                        aisleListViewModel,
                        onAisleClick = { id -> navController.navigate("aisleDetail/$id") },
                        onMedicinesClick = { navController.navigate(MEDICINE_LIST_ROUTE) }
                    )
                }
                composable(AISLE_DETAIL_ROUTE) {
                    AisleDetailScreen(
                        aisleDetailViewModel,
                        onMedicineClick = { id -> navController.navigate("medicineDetail/$id") },
                        onBackClick = { navController.navigateUp() }
                    )
                }
                composable(MEDICINE_LIST_ROUTE) {
                    MedicineListScreen(
                        medicineListViewModel,
                        onMedicineClick = { id -> navController.navigate("medicineDetail/$id") },
                        onAislesClick = { navController.navigate(AISLE_LIST_ROUTE) }
                    )
                }
                //composable(MEDICINE_DETAIL_ROUTE) {
                   // MedicineDetailScreen(
                     //   "name",
                       // medicineListViewModel
                    //)
                //}
            }
        }
    }
}

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

@Composable
fun EmbeddedSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    isSearchActive: Boolean,
    onActiveChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    var searchQuery by rememberSaveable { mutableStateOf(query) }
    val activeChanged: (Boolean) -> Unit = { active ->
        searchQuery = ""
        onQueryChange("")
        onActiveChanged(active)
    }

    val shape: Shape = RoundedCornerShape(16.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 16.dp)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isSearchActive) {
            IconButton(onClick = { activeChanged(false) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        BasicTextField(
            value = searchQuery,
            onValueChange = { query ->
                searchQuery = query
                onQueryChange(query)
            },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            singleLine = true,
            decorationBox = { innerTextField ->
                if (searchQuery.isEmpty()) {
                    Text(
                        text = "Search",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
                innerTextField()
            }
        )

        if (isSearchActive && searchQuery.isNotEmpty()) {
            IconButton(onClick = {
                searchQuery = ""
                onQueryChange("")
            }) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}