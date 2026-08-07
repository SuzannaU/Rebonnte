package com.openclassrooms.rebonnte.ui.model

import androidx.annotation.StringRes
import com.openclassrooms.rebonnte.R

enum class SortOption(@get:StringRes val labelId: Int) {
    NAME_ASCENDING(R.string.name_a_z),
    NAME_DESCENDING(R.string.name_z_a),
    STOCK_ASCENDING(R.string.stock_ascending),
    STOCK_DESCENDING(R.string.stock_descending)
}