package com.openclassrooms.rebonnte.ui.aisleDetail

import androidx.lifecycle.ViewModel
import com.openclassrooms.rebonnte.domain.model.Medicine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AisleDetailViewModel() : ViewModel() {

    private var _medicines = MutableStateFlow<List<Medicine>>(emptyList())
    val medicines = _medicines.asStateFlow()

    init {
        _medicines.value = ArrayList()
    }
}