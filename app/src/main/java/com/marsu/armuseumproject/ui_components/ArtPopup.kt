package com.marsu.armuseumproject.ui_components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.marsu.armuseumproject.R
import com.marsu.armuseumproject.database.Artwork

// TODO: Dismiss by clicking outside of the Popup area

@Composable
fun ArtPopup(art: Artwork, onDismiss: () -> Unit) {
    val imageUri = art.primaryImage.toUri()
    /*Popup(
        alignment = Alignment.Center,
        onDismissRequest = { Log.d("DISMISS", "Dismiss request detected") }, content = {
            Card(
                colors = CardColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.secondary,
                    disabledContentColor = MaterialTheme.colorScheme.tertiary
                ), modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, end = 20.dp, top = 60.dp, bottom = 60.dp)
            ) {
                Text(text = "At least this works")
            }
        })*/
    Log.d("POPUP art", art.toString())

    Dialog(
        content = {
            Card(
                colors = CardColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.secondary,
                    disabledContentColor = MaterialTheme.colorScheme.tertiary
                ),
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 120.dp, bottom = 147.dp)
                    .fillMaxSize()
                //.align(alignment = Alignment.CenterHorizontally)
            ) {
                /*Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {*/
                Image(
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.height(197.dp),
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = null
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-4).dp),
                    onClick = { /*TODO*/ },
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    Text(text = stringResource(id = R.string.save_image))
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(fontWeight = FontWeight.Bold, text = art.title)
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            horizontal = 32.dp,
                            vertical = 16.dp
                        )
                    )
                    Text(
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 60.dp),
                        text = art.artistDisplayName
                    )
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally),
                        onClick = { onDismiss() },
                        shape = MaterialTheme.shapes.extraSmall
                    ) {
                        Text(text = stringResource(id = R.string.back))
                    }
                }
                //}
            }
        },
        onDismissRequest = {
            Log.d("DISMISS", "Dismiss request detected")
            onDismiss()
        })

}
