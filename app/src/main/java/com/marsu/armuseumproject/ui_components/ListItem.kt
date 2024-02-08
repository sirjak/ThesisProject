package com.marsu.armuseumproject.ui_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.marsu.armuseumproject.R
import com.marsu.armuseumproject.database.Artwork

@Composable
fun ListItem(art: Artwork, modifier: Modifier = Modifier) {
    Row {
        Column {
            Image(painter = painterResource(id = R.drawable.ic_not_found_vector), contentDescription = null)
        }
    }
}