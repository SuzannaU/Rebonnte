package com.openclassrooms.rebonnte.ui.medicineDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.useCase.ArchiveMedicineUseCase
import com.openclassrooms.rebonnte.domain.useCase.GetHistoriesByMedicineUseCase
import com.openclassrooms.rebonnte.domain.useCase.GetMedicineByIdUseCase
import com.openclassrooms.rebonnte.domain.useCase.GetUsernameByIdUseCase
import com.openclassrooms.rebonnte.domain.util.DataResult
import com.openclassrooms.rebonnte.ui.DispatcherProvider
import com.openclassrooms.rebonnte.ui.model.HistoryUi
import com.openclassrooms.rebonnte.ui.util.toUi
import com.openclassrooms.rebonnte.ui.util.UiText
import com.openclassrooms.rebonnte.ui.util.toErrorMessageId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MedicineDetailViewModel(
    private val getMedicineById: GetMedicineByIdUseCase,
    private val getHistoriesByMedicine: GetHistoriesByMedicineUseCase,
    private val getUsernameById: GetUsernameByIdUseCase,
    private val archiveMedicine: ArchiveMedicineUseCase,
    private val dispatcher: DispatcherProvider,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val medicineId: String = savedStateHandle["medicineId"] ?: ""

    private var _uiState = MutableStateFlow<MedicineDetailState>(MedicineDetailState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadMedicine()
    }

    private fun loadMedicine() {
        viewModelScope.launch(dispatcher.io) {
            _uiState.value = MedicineDetailState.Loading
            combine(
                getMedicineById(medicineId),
                getHistoriesByMedicine(medicineId)
            ) { medicine, histories ->
                medicine to histories
            }
                .catch { e ->
                    onLoadFailure(exception = e)
                }
                .collect { (medicine, histories) ->
                    onLoadSuccess(medicine, histories)
                }
        }
    }

    private suspend fun onLoadSuccess(medicine: Medicine?, histories: List<History>) {
        if (medicine != null) {
            val medicineUi = medicine.toUi()

            val historiesUi: List<HistoryUi> = histories.map { history ->
                history.toUi(getUsernameByUserIdUtil(history.userId))
            }

            _uiState.value = MedicineDetailState.MedicineFound(
                medicine = medicineUi,
                histories = historiesUi,
            )
        } else {
            _uiState.value = MedicineDetailState.MedicineNotFound
        }
    }

    private fun onLoadFailure(exception: Throwable) {
        _uiState.value = MedicineDetailState.Error(exception.toErrorMessageId())

    }

    private suspend fun getUsernameByUserIdUtil(userId: String) : UiText {
        return when (val usernameResult = getUsernameById(userId)) {
            is DataResult.Failure -> {
                UiText.StringResource(R.string.unknown_user)
            }
            is DataResult.Success -> {
                usernameResult.data?.let {
                    UiText.RawString(it)
                } ?: UiText.StringResource(R.string.unknown_user)
            }
        }
    }

    fun onArchiveClick() {
        viewModelScope.launch {
            when (val result = archiveMedicine(medicineId)) {
                is DataResult.Success -> {
                    return@launch
                }

                is DataResult.Failure -> {
                    setErrorMessage(result.exception.toErrorMessageId())
                }
            }
        }
    }

    fun setErrorMessage(messageId: Int) {
        _uiState.value = MedicineDetailState.Error(messageId)
    }
}