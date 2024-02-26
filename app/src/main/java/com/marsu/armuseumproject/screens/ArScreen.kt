package com.marsu.armuseumproject.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.marsu.armuseumproject.R
import com.marsu.armuseumproject.database.Artwork
import com.marsu.armuseumproject.ui.theme.ARMuseumProjectTheme
import com.marsu.armuseumproject.viewmodels.ArActivityViewModel
import io.github.sceneview.ar.ARScene

class ArScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ARMuseumProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ArScreen()
                }
            }
        }
    }
}

@Composable
fun ArScreen(
    art: Artwork,
    viewModel: ArActivityViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val currentImageNode by viewModel.currentImageNode.observeAsState()

    ARScene(
        modifier = Modifier.fillMaxSize(),
    )

    Button(onClick = { /*TODO*/ }) {
        Text(text = stringResource(id = R.string.back))
    }
    Button(
        enabled = if (currentImageNode != null) true else false,
        onClick = { /*TODO*/ }) {
        Text(text = stringResource(id = R.string.delete_current))
    }
}