package com.jane.drakorapp.model

data class Drakor (
    val id: Int,
    val title: String,
    val photoUrl: String,
    val year: String,
    val description: String,
    val isFavorite: Boolean = false,
)