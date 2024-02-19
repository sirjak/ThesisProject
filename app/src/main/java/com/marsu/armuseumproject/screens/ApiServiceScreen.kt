package com.marsu.armuseumproject.screens

import ArtPopup
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.marsu.armuseumproject.MyApp
import com.marsu.armuseumproject.R
import com.marsu.armuseumproject.database.Artwork
import com.marsu.armuseumproject.database.PreferencesManager
import com.marsu.armuseumproject.fragments.SHARED_KEY
import com.marsu.armuseumproject.ui.theme.ARMuseumProjectTheme
import com.marsu.armuseumproject.ui_components.ArtItem
import com.marsu.armuseumproject.viewmodels.ApiServiceViewModel
import java.lang.reflect.Type

class ApiServiceScreen : ComponentActivity() {
    private lateinit var viewModel: ApiServiceViewModel
    private var lastFive = mutableListOf<Int>() // initiate variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ApiServiceViewModel(Application())

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
                    ApiServiceScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun ApiServiceScreen(
    viewModel: ApiServiceViewModel
) {
    val focusManager = LocalFocusManager.current

    val searchText by viewModel.searchText.collectAsState()
    val isLoading by viewModel.loadingResults.observeAsState()
    val resultsText by viewModel.resultText.observeAsState()
    val noResultText = stringResource(id = R.string.no_result)

    val artworks by viewModel.artsList.observeAsState()
    val initialBatch by viewModel.initialBatchLoaded.observeAsState()

    val showInfo by viewModel.isTesting.collectAsState()
    var boobs by remember { mutableStateOf<Artwork?>(null) }
    //val showInfo by remember { mutableStateOf(true) }

    // Starts search, dismisses the keyboard and clears focus from the TextField
    fun launchSearch() {
        viewModel.searchArtsWithInput()
        focusManager.clearFocus()
    }

    /**
     * Whole screen
     */
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = { focusManager.clearFocus() })
        }) {
        /**
         * Search area
         */
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(bottom = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            OutlinedTextField(colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                focusedIndicatorColor = MaterialTheme.colorScheme.background,
                focusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
                unfocusedTextColor = MaterialTheme.colorScheme.primary
            ),
                keyboardActions = KeyboardActions(onDone = {
                    launchSearch()
                }),
                leadingIcon = {
                    IconButton(content = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_search_32),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }, onClick = {
                        launchSearch()
                    })
                },
                modifier = Modifier
                    .padding(start = 10.dp, top = 10.dp)
                    .onKeyEvent {
                        if (it.nativeKeyEvent.keyCode == android.view.KeyEvent.KEYCODE_ENTER) {
                            launchSearch()
                        }
                        false
                    },
                placeholder = {
                    Text(
                        color = MaterialTheme.colorScheme.primary,
                        text = stringResource(id = R.string.search_arts)
                    )
                },
                shape = MaterialTheme.shapes.extraLarge,
                singleLine = true,
                value = searchText,
                onValueChange = viewModel::onSearchTextChange
            )

            TextButton(modifier = Modifier.padding(end = 10.dp), onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_filter_alt_24),
                    contentDescription = null
                )
                Text(text = stringResource(id = R.string.filter))
            }
        }

        /**
         * Result area
         */
        HorizontalDivider(modifier = Modifier.shadow(1.dp))

        if (isLoading == true && initialBatch == false) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(all = 20.dp)
                    .width(64.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 8.dp,
                trackColor = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            (if (resultsText !== "") resultsText else noResultText)?.let {
                Text(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(all = 10.dp),
                    text = it,
                    textAlign = TextAlign.Center
                )
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                if (!artworks.isNullOrEmpty()) {
                    itemsIndexed(artworks!!) { index, art ->
                        ArtItem(art = art,
                            modifier = Modifier
                                .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                                .clickable {
                                    boobs = art
                                    viewModel.onArtItemClick()
                                }
                                .fillMaxWidth())

                        // Loading more items when getting closer to the bottom
                        if (index >= artworks!!.size - 3) {
                            if ((viewModel.loadingResults.value == false)) {
                                viewModel.getArts(false)
                            }
                        }
                    }
                }
            }
        }
        if (showInfo && boobs !== null) {
            ArtPopup(boobs!!) { viewModel.onDismissPopup() }
        }
    }
}