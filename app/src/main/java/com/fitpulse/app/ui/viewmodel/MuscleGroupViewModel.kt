package com.fitpulse.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitpulse.app.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MuscleGroupUiState(
    val isLoading: Boolean = false,
    val categories: List<String> = emptyList(),
    val muscles: List<String> = emptyList(),
    val error: String? = null
)

class MuscleGroupViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MuscleGroupUiState())
    val uiState: StateFlow<MuscleGroupUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        _uiState.value = MuscleGroupUiState(isLoading = true)
        viewModelScope.launch {
            try {
                val categories = RetrofitClient.api.getCategories().results
                    .map { it.name }
                val muscles = RetrofitClient.api.getMuscles().results
                    .map { it.displayName }
                    .filter { it.isNotBlank() }
                    .distinct()
                _uiState.value = MuscleGroupUiState(
                    isLoading = false,
                    categories = categories,
                    muscles = muscles
                )
            } catch (e: Exception) {
                _uiState.value = MuscleGroupUiState(
                    isLoading = false,
                    error = "Could not load muscle groups. Check your connection."
                )
            }
        }
    }
}
