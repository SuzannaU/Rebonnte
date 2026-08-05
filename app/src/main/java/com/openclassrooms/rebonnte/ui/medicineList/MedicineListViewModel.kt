package com.openclassrooms.rebonnte.ui.medicineList

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.ui.model.MedicineUi
import com.openclassrooms.rebonnte.ui.model.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MedicineListViewModel(
    private val medicineRepository: MedicineRepository,
) : ViewModel() {

    private val _medicinesFlow = medicineRepository.getMedicines()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    private val _sortOption = MutableStateFlow(SortOption.NAME_ASCENDING)
    val sortOption = _sortOption.asStateFlow()

    val sortOptions = SortOption.entries

    fun sortMedicinesBy(sortOption: SortOption) {
        _sortOption.value = sortOption
    }

    val listScreenState: StateFlow<MedicineListScreenState> = combine(
        _medicinesFlow,
        _searchQuery,
        _sortOption,
    ) { medicines, query, sortOption ->

        val filteredMedicines: List<MedicineUi> =
            if (query.isEmpty()) {
                medicines.map {
                    it.toUi()
                }
            } else {
                medicines.filter { medicine ->
                    medicine.name.contains(query, ignoreCase = true)
                }.toList().map {
                    it.toUi()
                }
            }

        val sortedMedicines: List<MedicineUi> = when (sortOption) {
            SortOption.NAME_ASCENDING ->
                filteredMedicines.sortedBy { it.name }

            SortOption.NAME_DESCENDING ->
                filteredMedicines.sortedByDescending { it.name }

            SortOption.STOCK_ASCENDING ->
                filteredMedicines.sortedBy { it.currentStock }

            SortOption.STOCK_DESCENDING ->
                filteredMedicines.sortedByDescending { it.currentStock }
        }

        when {
            medicines.isEmpty() -> MedicineListScreenState.NoMedicinesFound
            filteredMedicines.isEmpty() -> MedicineListScreenState.NoResultFound
            else -> MedicineListScreenState.MedicinesFound(sortedMedicines)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MedicineListScreenState.Loading
    )
}

enum class SortOption(@get:StringRes val labelId: Int) {
    NAME_ASCENDING(R.string.name_a_z),
    NAME_DESCENDING(R.string.name_z_a),
    STOCK_ASCENDING(R.string.stock_ascending),
    STOCK_DESCENDING(R.string.stock_descending)
}

