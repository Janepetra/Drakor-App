package com.jane.drakorapp.data

import com.jane.drakorapp.model.Drakor
import com.jane.drakorapp.model.DrakorData
import kotlinx.coroutines.flow.Flow

//repo for penggunaan coroutines menggunakan flow
interface Repository {
    fun getDrakor(): List<Drakor> {
        return DrakorData.dummyDrakor
    }

    fun searchDrakor(query: String): Flow<List<Drakor>>

    fun getDrakorById(id: Int): Flow<Drakor>

    fun getFavDrakor(): Flow<List<Drakor>>

    fun updateFavDrakor(id: Int, isFavorite: Boolean): Flow<Boolean>
}