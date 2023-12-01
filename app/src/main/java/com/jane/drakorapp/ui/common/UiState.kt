package com.jane.drakorapp.ui.common

//sealed class untuk mengetahui status halaman, apakah sukses, error, or loading
sealed class UiState<out T: Any?> {

    object Loading : UiState<Nothing>()

    data class Success<out T: Any>(val data: T) : UiState<T>()

    data class Error(val errorMessage: String) : UiState<Nothing>()
}