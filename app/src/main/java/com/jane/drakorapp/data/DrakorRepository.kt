package com.jane.drakorapp.data

import com.jane.drakorapp.model.Drakor
import com.jane.drakorapp.model.DrakorData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

//repo for data source
@Singleton
class DrakorRepository @Inject constructor(): Repository {

    private val data = mutableListOf<Drakor>()

    init {
        if (data.isEmpty()) {
            data.addAll(DrakorData.dummyDrakor)
        }
    }
    override fun searchDrakor(query: String) = flow {
        val DrakorData = data.filter {
            it.title.contains(query, ignoreCase = true)
        }
        emit(DrakorData)
    }

    override fun getDrakorById(DrakorId: Int): Flow<Drakor> {
        return flowOf (data.first {
            it.id == DrakorId
        })
    }
    override fun updateFavDrakor(DrakorId: Int, isFavorite: Boolean): Flow<Boolean> {
        val index = data.indexOfFirst { it.id == DrakorId }
        val result = if (index >= 0) {
            val drakor = data[index]
            data[index] =
                drakor.copy(isFavorite = isFavorite)
            true
        } else {
            false
        }
        return flowOf(result)
    }
    override fun getFavDrakor(): Flow<List<Drakor>> {
        return flowOf(data.filter {it.isFavorite})
    }
}