package com.marsu.armuseumproject.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.google.gson.Gson
import com.marsu.armuseumproject.MyApp
import com.marsu.armuseumproject.R
import com.marsu.armuseumproject.activities.ArActivity
import com.marsu.armuseumproject.database.Artwork
import com.marsu.armuseumproject.database.PreferencesManager
import com.marsu.armuseumproject.database.SHARED_KEY
import com.marsu.armuseumproject.ui_components.ArtItem
import com.marsu.armuseumproject.viewmodels.ArSelectionViewModel

@Composable
fun ArSelectionScreen(
    lastFive: MutableList<Int>, viewModel: ArSelectionViewModel
) {
    val preferencesManager = remember { PreferencesManager(MyApp.appContext) }
    val context = LocalContext.current

    val artworks by viewModel.getAllArtwork.observeAsState()
    val preselectId by viewModel.preselectedId.collectAsState()

    val noArtChosenText = stringResource(id = R.string.none)
    var chosenArt by remember { mutableStateOf<Uri?>(null) }
    var chosenArtist by remember { mutableStateOf("") }
    var chosenId by remember { mutableStateOf<Int?>(null) }
    var chosenTitle by remember { mutableStateOf(noArtChosenText) }
    var isSelected by remember { mutableStateOf(false) }

    fun selectArt(art: Artwork) {
        chosenArtist = art.artistDisplayName
        chosenId = art.objectID
        chosenTitle = art.title
        isSelected = true
        viewModel.saveId(null)
    }

    // Handling preselection when navigated from HomeScreen by clicking an ArtItem
    if (preselectId !== null) {
        val art = viewModel.getArt(preselectId!!).observeAsState().value
        art?.get(0)?.let { selectArt(it) }
    }

    fun postValuesToViewModel(id: Int, uri: Uri) {
        viewModel.imageUri.postValue(uri)
        viewModel.imageId.postValue(id)
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
        preferencesManager.saveData(SHARED_KEY, storedLastFive)
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top
    ) {
        artworks?.let {
            itemsIndexed(it) { _, art ->
                ArtItem(art = art,
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                        .clickable {
                            chosenArt = art.primaryImage.toUri()
                            postValuesToViewModel(art.objectID, art.primaryImage.toUri())
                            selectArt(art)
                        }
                        .fillMaxWidth())
            }
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = stringResource(id = R.string.chosen_artwork)
        )
        Text(text = chosenTitle)
        Text(text = chosenArtist)
        Button(
            enabled = isSelected, onClick = {
                val id = viewModel.imageId.value
                if (id !== null) {
                    addToList(id)
                    addToSharedPrefs()
                }
                context.startActivity(Intent(context, ArActivity::class.java))
            }, modifier = Modifier.padding(all = 20.dp)
        ) {
            Text(text = stringResource(id = R.string.start_ar))
        }
    }
}