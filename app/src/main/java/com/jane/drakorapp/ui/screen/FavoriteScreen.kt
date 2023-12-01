package com.jane.drakorapp.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.jane.drakorapp.model.Drakor
import com.jane.drakorapp.ui.common.UiState
import com.jane.drakorapp.viewmodel.FavoriteViewModel

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    navigateToDetail: (Int) -> Unit,
    viewModel: FavoriteViewModel = hiltViewModel()
) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let {
        when(it) {
            is UiState.Loading -> {
                viewModel.getFavDrakor()
            }
            is UiState.Success -> {
                FavoriteContent(
                    modifier = modifier,
                    listDrakor = it.data,
                    navigateToDetail = navigateToDetail,
                    onFavoriteIconClicked = { id, isFavorite ->
                        viewModel.updateFavDrakor(id, isFavorite)
                    }
                )
            }
            is UiState.Error -> {
                EmpyList()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FavoriteContent(
    modifier: Modifier = Modifier,
    listDrakor: List<Drakor>,
    navigateToDetail: (Int) -> Unit,
    onFavoriteIconClicked: (id: Int, newState: Boolean) -> Unit,
) {
    Scaffold(
        topBar = { TopBar() },
        modifier = modifier
    ) { innerPadding ->
        //get fav data using homeContent list it's supposed from lazyList
        Column (modifier = modifier.padding(innerPadding)) {
            when (listDrakor.isEmpty()) {
                true -> EmpyList()
                false -> HomeContent(
                    listDrakor = listDrakor,
                    navigateToDetail = navigateToDetail,
                    onFavoriteIconClicked = onFavoriteIconClicked,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
        Text(text = "Favorite Drakor")
    },
        modifier = modifier)
}