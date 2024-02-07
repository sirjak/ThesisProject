package com.marsu.armuseumproject.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import coil.compose.rememberAsyncImagePainter
import com.marsu.armuseumproject.MyApp
import com.marsu.armuseumproject.R
import com.marsu.armuseumproject.database.Artwork
import com.marsu.armuseumproject.databinding.FragmentSelectFromGalleryBinding
import com.marsu.armuseumproject.viewmodels.SelectFromGalleryViewModel
import java.util.UUID

/**
 * Fragment for adding custom artwork to the application. Has two input fields for the title and author,
 * as well as a image input for the artwork itself. Artwork is saved to the Room database.
 */
class SelectFromGallery : Fragment() {
    companion object {
        private lateinit var viewModel: SelectFromGalleryViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = SelectFromGalleryViewModel()
        val binding = DataBindingUtil.inflate<FragmentSelectFromGalleryBinding>(
            inflater,
            R.layout.fragment_select_from_gallery,
            container,
            false
        ).apply {
            composeView.setContent {
                MaterialTheme {
                    SelectFromGalleryScreen(viewModel = viewModel)
                }
            }
        }
        return binding.root
    }
}

@Composable
fun SelectFromGalleryScreen(viewModel: SelectFromGalleryViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    val defaultImage = painterResource(id = R.drawable.ic_baseline_add_a_photo_24)
    var entryId = 0
    var imageArtist by remember { mutableStateOf("") }
    var imageTitle by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val toastText = stringResource(id = R.string.pickImageToast)

    /**
     * Open selection from gallery and set selected image to imageUri
     */
    val openGallery = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            if (it !== null) imageUri = it
        }
    )

    fun insertToDatabase(
        viewModel: SelectFromGalleryViewModel,
        uri: Uri?,
        title: String,
        artist: String,
    ) {
        viewModel.insertImage(
            Artwork(
                entryId,
                uri.toString(),
                uri.toString(),
                "",
                title,
                artist,
                ""
            )
        )
        Toast.makeText(MyApp.appContext, "Image saved", Toast.LENGTH_SHORT).show()
    }
    // TODO: Implement clearing focus/closing keyboard when user clicks outside TextField
    /* fun closeKeyBoard(view: View) {
        val imm: InputMethodManager =
            view.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }*/

    fun clearTextFields() {
        imageUri = null
        imageTitle = ""
        imageArtist = ""
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        /**
         * Image selection
         */
        IconButton(
            content = {
                Image(
                    painter = if (imageUri != null) rememberAsyncImagePainter(imageUri) else defaultImage,
                    contentDescription = null,
                    modifier = Modifier.size(height = 250.dp, width = 250.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .size(height = 250.dp, width = 250.dp),
            onClick = {
                openGallery.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        )
        /**
         * Image info
         */
        OutlinedTextField(
            label = { Text(text = stringResource(id = R.string.title)) },
            onValueChange = { imageTitle = it },
            supportingText = { Text(text = "* required") },
            value = imageTitle
        )
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        OutlinedTextField(
            label = { Text(text = stringResource(id = R.string.artist)) },
            onValueChange = { imageArtist = it },
            value = imageArtist
        )

    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        /**
         * Save button
         */
        Button(
            modifier = Modifier.padding(bottom = 35.dp),
            onClick = {
                if (imageUri == null || imageTitle == "") {
                    Toast.makeText(
                        MyApp.appContext,
                        toastText,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    entryId = UUID.randomUUID().hashCode() * -1
                    insertToDatabase(
                        viewModel,
                        imageUri,
                        imageTitle,
                        imageArtist
                    )
                    clearTextFields()
                }
            }
        ) {
            Text(text = stringResource(id = R.string.saveButton))
        }
    }
}