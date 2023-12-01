package com.jane.drakorapp.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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

//view model for management state
@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val _uiState: MutableStateFlow<UiState<List<Drakor>>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<Drakor>>>
        get() = _uiState

    private val _groupedDrakor = MutableStateFlow(
        repository.getDrakor()
            .sortedBy { it.title }
            .groupBy { it.title[0] }
    )
    val groupedDrakor: StateFlow<Map<Char, List<Drakor>>> get() = _groupedDrakor

    private val _query = mutableStateOf("")
    val query: State<String> get() =_query

    fun searchDrakor(newQuery: String) = viewModelScope.launch {
        _query.value = newQuery
        repository.searchDrakor(_query.value)
            .catch {
                _uiState.value = UiState.Error(it.message.toString())
            }
            .collect{
                _uiState.value = UiState.Success(it)
            }
    }

    fun updateFavDrakor(id: Int, newState: Boolean) = viewModelScope.launch {
        repository.updateFavDrakor(id, newState)
            .collect { isUpdated ->
                if (isUpdated) searchDrakor(_query.value)
            }
    }
}