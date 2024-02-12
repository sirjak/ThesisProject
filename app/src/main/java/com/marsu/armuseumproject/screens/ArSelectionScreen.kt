package com.marsu.armuseumproject.screens

import android.app.Application
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.google.gson.Gson
import com.marsu.armuseumproject.MyApp
import com.marsu.armuseumproject.R
import com.marsu.armuseumproject.database.PreferencesManager
import com.marsu.armuseumproject.fragments.SHARED_KEY
import com.marsu.armuseumproject.ui.theme.ARMuseumProjectTheme
import com.marsu.armuseumproject.ui_components.ArtItem
import com.marsu.armuseumproject.viewmodels.ArSelectionViewModel

class ArSelectionScreen : ComponentActivity() {
    private lateinit var arSelectionViewModel: ArSelectionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arSelectionViewModel = ArSelectionViewModel(Application())

        setContent {
            ARMuseumProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //ArSelectionScreen(arSelectionViewModel)
                }
            }
        }
    }
}

@Composable
fun ArSelectionScreen(lastFive: MutableList<Int>, viewModel: ArSelectionViewModel) {
    val preferencesManager = remember { PreferencesManager(MyApp.appContext) }

    val artworks by viewModel.getAllArtwork.observeAsState()

    val noArtChosenText = stringResource(id = R.string.none)
    var chosenArt by remember { mutableStateOf<Uri?>(null) }
    var chosenId by remember { mutableStateOf<Int?>(null) }
    var chosenText by remember { mutableStateOf(noArtChosenText) }
    var isSelected by remember { mutableStateOf(false) }

    if (chosenArt !== null) {
        viewModel.imageUri.postValue(chosenArt)
        viewModel.imageId.postValue(chosenId)
    }

    // Saving latest watched artwork id into MutableList<Int>
    // Checks if the id already exist in the list
    // If it does, removes the old one from the collection before adding it again as the latest
    fun addToList(id: Int) {
        if (lastFive.contains(id)) {
            val index = lastFive.indexOf(id)
            lastFive.removeAt(index)
        }

        lastFive.add(0, id)

        // Keeps only 5 most recent in the list
        if (lastFive.size > 5) {
            lastFive.removeLast()
        }
    }

    // Converts lastFive (list of Artwork id's) to a json and stores to shared preferences
    fun addToSharedPrefs() {
        val storedLastFive = Gson().toJson(lastFive)
        Log.d("STORED", storedLastFive)
        preferencesManager.saveData(SHARED_KEY, storedLastFive)
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        artworks?.let {
            itemsIndexed(it) { index, art ->
                ArtItem(art = art, modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                    .clickable {
                        isSelected = true
                        chosenArt = art.primaryImage.toUri()
                        chosenId = art.objectID
                        chosenText = art.title
                    }
                    .fillMaxWidth()
                )
            }
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = stringResource(id = R.string.chosen_artwork)
        )
        Text(text = chosenText)
        Button(
            enabled = isSelected,
            onClick = {
                val id = viewModel.imageId.value
                if (id !== null) {
                    addToList(id)
                    addToSharedPrefs()
                }
            },
            modifier = Modifier.padding(all = 20.dp)
        ) {
            Text(text = stringResource(id = R.string.start_ar))
        }
    }
}