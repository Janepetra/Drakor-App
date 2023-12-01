package com.jane.drakorapp.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.jane.drakorapp.R
import com.jane.drakorapp.model.Drakor
import com.jane.drakorapp.ui.common.UiState
import com.jane.drakorapp.ui.theme.DrakorAppTheme
import com.jane.drakorapp.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToDetail: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val query by viewModel.query
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let {
        when(it) {
            is UiState.Loading -> {
                viewModel.searchDrakor(query)
            }
            is UiState.Success -> {
                HomeContent(
                    listDrakor = it.data,
                    modifier = modifier,
                    navigateToDetail = navigateToDetail,
                    onFavoriteIconClicked = viewModel::updateFavDrakor,
                )
            }
            is UiState.Error -> {
                EmpyList()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    listDrakor: List<Drakor>,
    navigateToDetail: (Int) -> Unit,
    onFavoriteIconClicked: (id: Int, isFavorite: Boolean) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val groupedDrakor by viewModel.groupedDrakor.collectAsState()
    val query by viewModel.query

    Box(modifier = modifier) {
        //rememberCoroutineScope digunakan untuk menjalankan suspend function di dalam Composable function.
        val scope = rememberCoroutineScope()
        //rememberLazyListState merupakan state dari Lazy List yang digunakan untuk mengontrol dan membaca posisi item.
        val listState = rememberLazyListState()
        //showButton akan menyimpan state menggunakan derivedStateOf ketika index item pertama sudah tidak terlihat.
        val showButton: Boolean by remember {
            derivedStateOf { listState.firstVisibleItemIndex > 0 }
        }
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            //item: digunakan untuk menambahkan sebuah item
            item {
                SearchBar(
                    query = query,
                    onQueryChange = viewModel::searchDrakor,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable {}
                )
            }
            groupedDrakor.forEach { (initial, item) ->
                stickyHeader {
                    CharacterHeader(initial)
                }
                //items: digunakan untuk menambahkan banyak item
                //mengambil data menggunakan key
                items(item, key = { it.id }) { drakor ->
                    DrakorListItem(
                        id = drakor.id,
                        name = drakor.title,
                        photoUrl = drakor.photoUrl,
                        year = drakor.year,
                        isFavorite = drakor.isFavorite,
                        onFavoriteIconClicked = onFavoriteIconClicked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement(tween(durationMillis = 100))
                            .clickable { navigateToDetail(drakor.id) }
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = showButton,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
            modifier = Modifier
                .padding(bottom = 30.dp)
                .align(Alignment.BottomCenter)
        ) {
            ScrollToTopButton(
                onClick = {
                    scope.launch {
                        //scrollToItem untuk menuju ke index item tertentu
                        listState.scrollToItem(index = 0)
                    }
                }
            )
        }
    }
}

@Composable
fun DrakorListItem(
    id: Int,
    name: String,
    photoUrl: String,
    year: String,
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
    onFavoriteIconClicked: (id: Int, isFavorite: Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box {
            AsyncImage(
                model = photoUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(8.dp)
                    .size(100.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = name,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            )
            Text(
                text = year,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            )
        }
        //icon for favorite fiture
        Icon(
            imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = null,
            tint = if (!isFavorite) Color.Black else Color.Red,
            modifier = Modifier
                .padding(16.dp)
                .size(24.dp)
                .clickable { onFavoriteIconClicked(id, !isFavorite) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HeroListThemePreview() {
    DrakorAppTheme {
        DrakorListItem(
            id = 1,
            name = "Mouse",
            photoUrl = "",
            year = "2021",
            isFavorite = true,
            onFavoriteIconClicked = {_, _ ->})
    }
}

@Composable
fun ScrollToTopButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilledIconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowUp,
            contentDescription = stringResource(R.string.scroll_to_top),
        )
    }
}

@Composable
fun CharacterHeader(
    char: Char,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
    ) {
        Text(
            text = char.toString(),
            fontWeight = FontWeight.Black,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(3.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = {},
        active = false,
        onActiveChange = {},
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        placeholder = {
            Text(stringResource(R.string.search_drakor))
        },
        shape = MaterialTheme.shapes.large,
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
            .heightIn(min = 48.dp)
    ) {
    }
}