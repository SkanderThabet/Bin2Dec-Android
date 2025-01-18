package com.skander.bin2dec.ui.main.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skander.bin2dec.domain.usecases.ConvertBinaryToDecimalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val convertBinaryToDecimalUseCase: ConvertBinaryToDecimalUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Initial)
    val uiState: StateFlow<MainUiState> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MainUiState.Initial
    )

    fun onBinaryInput(input: String) {
        viewModelScope.launch {
            _uiState.value = when {
                input.isEmpty() -> MainUiState.Initial
                else -> convertBinaryToDecimalUseCase(input)
                    .fold(
                        onSuccess = {
                            savedStateHandle[KEY_DECIMAL_VALUE] = it
                            MainUiState.Success(it)
                        },
                        onFailure = { MainUiState.Error(it.message ?: "Invalid input") }
                    )
            }
        }
    }

    companion object {
        private const val KEY_DECIMAL_VALUE = "decimal_value"
    }
}

sealed interface MainUiState {
    data object Initial : MainUiState
    data class Success(val decimal: Int) : MainUiState
    data class Error(val message: String) : MainUiState
}