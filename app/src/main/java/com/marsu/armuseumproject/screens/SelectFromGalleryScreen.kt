package com.marsu.armuseumproject.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.marsu.armuseumproject.MyApp
import com.marsu.armuseumproject.R
import com.marsu.armuseumproject.database.Artwork
import com.marsu.armuseumproject.service.InternalStorageService
import com.marsu.armuseumproject.viewmodels.SelectFromGalleryViewModel
import java.util.UUID

@Composable
fun SelectFromGalleryScreen(viewModel: SelectFromGalleryViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val focusManager = LocalFocusManager.current

    val defaultImage = painterResource(id = R.drawable.ic_baseline_add_a_photo_24)
    var entryId = 0
    var imageArtist by remember { mutableStateOf("") }
    var imageTitle by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val toastText = stringResource(id = R.string.pickImageToast)

    /**
     * Open selection from gallery and set selected image to imageUri
     */
    val openGallery =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { if (it !== null) imageUri = it })

    fun insertToDatabase(
        viewModel: SelectFromGalleryViewModel,
        uri: Uri?,
        title: String,
        artist: String,
    ) {
        viewModel.insertImage(
            Artwork(
                entryId, uri.toString(), uri.toString(), "", title, artist, ""
            )
        )
        Toast.makeText(MyApp.appContext, "Image saved", Toast.LENGTH_SHORT).show()
    }

    fun clearTextFields() {
        imageUri = null
        imageTitle = ""
        imageArtist = ""
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = { focusManager.clearFocus() })
        }) {
        /**
         * Image selection
         */
        IconButton(content = {
            Image(
                painter = if (imageUri != null) rememberAsyncImagePainter(imageUri) else defaultImage,
                contentDescription = stringResource(id = R.string.contentDescriptionSaveFromGallery),
                modifier = Modifier.fillMaxSize()
            )
        }, modifier = Modifier
            .fillMaxWidth()
            .size(height = 250.dp, width = 250.dp), onClick = {
            openGallery.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        })
        /**
         * Image info
         */
        OutlinedTextField(label = { Text(text = stringResource(id = R.string.title)) },
            onValueChange = { imageTitle = it },
            supportingText = { Text(text = "* required") },
            value = imageTitle
        )
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        OutlinedTextField(label = { Text(text = stringResource(id = R.string.artist)) },
            onValueChange = { imageArtist = it },
            value = imageArtist
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom
    ) {
        /**
         * Save button
         */
        Button(modifier = Modifier.padding(bottom = 35.dp), onClick = {
            if (imageUri == null || imageTitle == "") {
                Toast.makeText(
                    MyApp.appContext, toastText, Toast.LENGTH_SHORT
                ).show()
            } else {
                val newUri = InternalStorageService.saveFileToInternalStorage(imageUri)
                entryId = UUID.randomUUID().hashCode() * -1
                insertToDatabase(
                    viewModel, newUri, imageTitle, imageArtist
                )
                focusManager.clearFocus()
                clearTextFields()
            }
        }) {
            Text(text = stringResource(id = R.string.saveButton))
        }
    }
}