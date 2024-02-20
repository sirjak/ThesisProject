package com.marsu.armuseumproject.ui_components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.marsu.armuseumproject.R
import com.marsu.armuseumproject.database.Artwork

// TODO: Dismiss by clicking outside of the Popup area

@Composable
fun ArtPopup(art: Artwork, onDismiss: () -> Unit) {
    val imageUri = art.primaryImage.toUri()

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
                    .fillMaxWidth(0.90f)
                    .fillMaxHeight(0.80f),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    //horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.50f),
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
                        modifier = Modifier.fillMaxSize(),
                        //.fillMaxSize()
                        //.padding(top = 60.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Spacer(modifier = Modifier.fillMaxHeight(0.20f))
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
                        Spacer(modifier = Modifier.fillMaxHeight(0.20f))
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = 4.dp),
                            //.weight(2f, false),
                            onClick = { onDismiss() },
                            shape = MaterialTheme.shapes.extraSmall
                        ) {
                            Text(text = stringResource(id = R.string.back))
                        }
                    }

                }
            }
        },
        onDismissRequest = {
            Log.d("DISMISS", "Dismiss request detected")
            onDismiss()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    )

}
