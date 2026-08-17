package com.openclassrooms.rebonnte.ui.medicineList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.rebonnte.domain.useCase.GetMedicinesOrderedByUseCase
import com.openclassrooms.rebonnte.ui.DispatcherProvider
import com.openclassrooms.rebonnte.ui.model.MedicineUi
import com.openclassrooms.rebonnte.ui.model.SortOption
import com.openclassrooms.rebonnte.ui.util.toDomainSortOption
import com.openclassrooms.rebonnte.ui.util.toUi
import com.openclassrooms.rebonnte.ui.util.toErrorMessageId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class MedicineListViewModel(
    private val getMedicinesOrderedBy: GetMedicinesOrderedByUseCase,
    dispatcher: DispatcherProvider,
) : ViewModel() {


    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    private val _sortOption = MutableStateFlow(SortOption.NAME_ASCENDING)
    val sortOption = _sortOption.asStateFlow()
    fun onSortOptionSelected(sortOption: SortOption) {
        _sortOption.value = sortOption
    }

    private val _medicinesFlow = _sortOption.flatMapLatest { selectedOption ->
        getMedicinesOrderedBy(selectedOption.toDomainSortOption())
    }

    val listScreenState: StateFlow<MedicineListScreenState> = combine(
        flow = _medicinesFlow,
        flow2 = _searchQuery,
    ) { medicines, query ->

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

        MedicineListScreenState.MedicinesFound(filteredMedicines) as MedicineListScreenState
    }
        .flowOn(dispatcher.io)
        .catch { e ->
            emit(MedicineListScreenState.Error(e.toErrorMessageId()))
        }
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MedicineListScreenState.Loading
    )
}