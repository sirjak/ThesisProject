package com.marsu.armuseumproject.screens

import android.app.Application
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.marsu.armuseumproject.MyApp
import com.marsu.armuseumproject.database.Artwork
import com.marsu.armuseumproject.database.PreferencesManager
import com.marsu.armuseumproject.fragments.SHARED_KEY
import com.marsu.armuseumproject.ui.theme.ARMuseumProjectTheme
import com.marsu.armuseumproject.ui_components.ArtItem
import com.marsu.armuseumproject.viewmodels.HomeViewModel
import java.lang.reflect.Type

class HomeScreen : ComponentActivity() {
    private lateinit var viewModel: HomeViewModel
    private var lastFive = mutableListOf<Int>() // initiate variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = HomeViewModel(Application())

        // Retrieve lastFive from shared preferences and converting back to list from json
        val preferences = PreferencesManager(MyApp.appContext)
        val json = preferences.getData(SHARED_KEY, null)
        val type: Type = object : TypeToken<List<Int>>() {}.type
        if (json != null) {
            lastFive = Gson().fromJson(json, type)
        }

        setContent {
            ARMuseumProjectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    HomeScreen(lastFive, viewModel)
                }
            }
        }
    }
}

@Composable
fun HomeScreen(lastFive: MutableList<Int>, viewModel: HomeViewModel) {
    val preferencesManager = remember { PreferencesManager(MyApp.appContext) }
    val context = LocalContext.current

    Log.d("LAST", lastFive.toString())
    // Getting the artwork objects by id's collected in lastFive and sending info to recycler adapter
    val artworks = remember { mutableListOf<Artwork?>() }
    //val test = viewModel.getArt(lastFive[0]).observeAsState().value
    //Log.d("TEST", test.toString())
    for (i in lastFive.indices) {
        viewModel.getArt(lastFive[i]).observeAsState().value.let { artworks.add(it?.get(0)) }
    }
    Log.d("ARTWORKS", artworks.toString())
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Welcome")
        Text(text = if (lastFive.isEmpty()) "Instructions" else "Info")

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(artworks) { _, art ->
                if (art != null) {
                    ArtItem(art = art,
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                            .clickable { /*TODO */ }
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}
