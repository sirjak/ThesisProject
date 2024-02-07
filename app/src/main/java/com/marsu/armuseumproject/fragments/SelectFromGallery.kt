package com.marsu.armuseumproject.fragments

import android.content.Context.INPUT_METHOD_SERVICE
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.databinding.DataBindingUtil
import coil.compose.rememberAsyncImagePainter
import com.google.android.material.textfield.TextInputEditText
import com.marsu.armuseumproject.R
import com.marsu.armuseumproject.viewmodels.SelectFromGalleryViewModel
import com.marsu.armuseumproject.database.Artwork
import com.marsu.armuseumproject.databinding.FragmentSelectFromGalleryBinding
import java.util.*

/**
 * Fragment for adding custom artwork to the application. Has two input fields for the title and author,
 * as well as a image input for the artwork itself. Artwork is saved to the Room database.
 */
class SelectFromGallery : Fragment() {
    private var entryId: Int = 0
    private var resultUri: Uri? = null

    companion object {
        private lateinit var viewModel: SelectFromGalleryViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        entryId = UUID.randomUUID().hashCode() * -1
        val binding = DataBindingUtil.inflate<FragmentSelectFromGalleryBinding>(
            inflater,
            R.layout.fragment_select_from_gallery,
            container,
            false
        ).apply {
            composeView.setContent {
                MaterialTheme {
                    SelectFromGalleryScreen()
                }
            }
        }
        viewModel = SelectFromGalleryViewModel()

//        val saveButton: Button = binding.saveButton
//        val titleEditText = binding.inputTitle
//        val artistEditText = binding.inputArtist
//        val constraint = binding.ConstraintLayout
//
//        constraint.setOnClickListener {
//            clearFocuses(
//                titleEditText,
//                artistEditText,
//                constraint
//            )
//        }
//
//        saveButton.setOnClickListener {
//            if (resultUri == null || titleEditText.text.toString() == "") {
//                Toast.makeText(
//                    MyApp.appContext,
//                    getString(R.string.pickImageToast),
//                    Toast.LENGTH_SHORT
//                ).show()
//            } else {
//                val newUri = InternalStorageService.saveFileToInternalStorage(resultUri)
//                insertToDatabase(
//                    viewModel,
//                    newUri,
//                    titleEditText.text.toString(),
//                    artistEditText.text.toString()
//                )
//                clearEditTexts(
//                    titleEditText,
//                    artistEditText,
//                    imgView
//                )
//            }
//        }

        return binding.root
    }

    private fun insertToDatabase(
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
    }

    private fun clearEditTexts(
        title: TextInputEditText,
        artist: TextInputEditText,
        imgView: ImageView
    ) {
        title.setText("")
        artist.setText("")
        title.clearFocus()
        artist.clearFocus()
        imgView.setImageResource(R.drawable.ic_baseline_image_24)
        resultUri = null
    }

    private fun closeKeyBoard(view: View) {
        val imm: InputMethodManager =
            view.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Clear focus from inputs
     */
    private fun clearFocuses(
        title: TextInputEditText,
        artist: TextInputEditText,
        view: View
    ) {
        title.clearFocus()
        artist.clearFocus()
        closeKeyBoard(view)
    }
}

@Composable
fun SelectFromGalleryScreen() {

    val defaultImage = painterResource(id = R.drawable.ic_baseline_add_a_photo_24)
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    /**
     * Open selection from gallery and set selected image to imageUri
     */
    val openGallery = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            if (it !== null) imageUri = it
        }
    )

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        /**
         * Image selection
         */
        IconButton(
            content = {
                Image(painter = if (imageUri != null) rememberAsyncImagePainter(imageUri) else defaultImage, contentDescription = null, modifier = Modifier.size(height = 250.dp, width = 250.dp))
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
         * TODO: Add TextInputs for image name and artist
         */

        /**
         * TODO: Add Save button
         */
    }
}