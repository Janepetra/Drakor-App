package com.jane.drakorapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.jane.drakorapp.ui.common.UiState
import com.jane.drakorapp.ui.theme.DrakorAppTheme
import com.jane.drakorapp.viewmodel.DetailViewModel

@Composable
fun DetailScreen(
    DrakorId : Int,
    viewModel: DetailViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
) {

    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let {
        when (it) {
            is UiState.Loading -> {
                viewModel.getDrakorById(DrakorId)
            }
            is UiState.Success -> {
                val data = it.data
                DetailContent (
                    id = data.id,
                    photoUrl = data.photoUrl,
                    title = data.title,
                    year = data.year,
                    description = data.description,
                    isFavorite = data.isFavorite,
                    onBackClick = navigateBack,
                    onAddToFav = { id, isFavorite ->
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

@Composable
fun DetailContent(
    id: Int,
    photoUrl: String,
    title: String,
    year: String,
    description: String,
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    onAddToFav: (id: Int, isFavorite: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ){
        Row (
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top

        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = ("back"),
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { onBackClick() }
            )
            IconButton(
                onClick = {
                    onAddToFav(id, isFavorite)
                },
                modifier = Modifier
                    .padding(start = 280.dp, top = 5.dp),
            ) {
                //icon for fav fiture
                Icon(
                    modifier = modifier,
                    imageVector = if (!isFavorite) Icons.Default.FavoriteBorder else Icons.Default.Favorite,
                    contentDescription = if (!isFavorite) ("Add to Favorite") else ("Remove from Favorite"),
                    tint = if (!isFavorite) Color.Red else Color.Red,
                )
            }

        }
        AsyncImage(
            model = photoUrl,
            contentDescription = "drakor_image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(20.dp)
                .size(250.dp)
        )
        Row (modifier = modifier) {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                fontSize = 27.sp,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
            Text(
                text = "($year)",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(start = 14.dp)
                    .padding(top = 8.dp)

            )
        }
        Text(
            text = description,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetailContentPreview() {
    DrakorAppTheme {
        DetailContent(
            id = 2,
            "https://asianwiki.com/images/a/a7/My_Name-p2.jpg",
            "My Name",
            "2020",
            "Yoon Ji-Woo’s (Han So-Hee) father dies suddenly. She wants to desperately take revenge on whoever is responsible for her father's death. Yoon Ji-Woo works for drug crime group Dongcheonpa.Choi Mu-Jin (Park Hee-Soon) is the boss of the drug gang.",
            onBackClick = {},
            onAddToFav = {_, _ ->},
            isFavorite = true,
        )
    }
}