package com.jane.drakorapp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jane.drakorapp.R
import com.jane.drakorapp.ui.theme.DrakorAppTheme

@Composable
fun ProfileScreen (
    modifier: Modifier = Modifier,
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 200.dp)
    ){
        Image(
            painter = painterResource(R.drawable.foto),
            contentDescription = "photo_profile",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(20.dp)
                .size(200.dp)
                .clip(CircleShape)
        )

        Text(
            text = "Jane Petra Sirken",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            fontSize = 27.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        )
        Text(
            text = "janeptsk@gmail.com",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AboutActivityPreview() {
    DrakorAppTheme {
        ProfileScreen()
    }
}