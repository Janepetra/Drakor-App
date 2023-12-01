package com.jane.drakorapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jane.drakorapp.data.Repository
import com.jane.drakorapp.model.Drakor
import com.jane.drakorapp.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor ( private val repository: Repository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<Drakor>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<Drakor>>
        get() = _uiState

    fun getDrakorById(DrakorId: Int) {
        viewModelScope.launch {
            repository.getDrakorById(DrakorId)
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())}
                .collect {
                    _uiState.value = UiState.Success(it)}
        }
    }
    fun updateFavDrakor(id: Int, isFavorite: Boolean) =
        viewModelScope.launch {
            repository.updateFavDrakor(id, !isFavorite)
                .collect { update ->
                    if (update) getDrakorById(id)
                }
        }
}