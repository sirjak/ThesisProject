package com.marsu.armuseumproject.ui_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.marsu.armuseumproject.R
import com.marsu.armuseumproject.database.Artwork

@Composable
fun ArtItem(art: Artwork, modifier: Modifier = Modifier) {
    val imageUri = art.primaryImage.toUri()

    // TODO: Make the corners round by 10dp
    androidx.compose.material3.ListItem(headlineContent = {
        Column(horizontalAlignment = Alignment.Start) {
            Text(fontWeight = FontWeight.Bold, text = art.title)
            Text(text = art.artistDisplayName)
        }
    }, modifier = modifier, shadowElevation = 3.5.dp, trailingContent = {
        Image(
            contentDescription = stringResource(id = R.string.contentDescriptionListItem),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = 100.dp, height = 100.dp)
                .offset(x = 7.dp, y = 0.dp),
            painter = rememberAsyncImagePainter(imageUri)
        )
    })
}