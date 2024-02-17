package com.marsu.armuseumproject.screens

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiServiceScreen(
    viewModel: ApiServiceViewModel
) {
    //val preferencesManager = remember { PreferencesManager(MyApp.appContext) }
    //val context = LocalContext.current

    val searchText by viewModel.searchText.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    val noResultText = stringResource(id = R.string.no_result)
    val resultsText = stringResource(id = R.string.results)

    var isLoading by remember { mutableStateOf(false) }
    val artworks = remember { mutableListOf<Artwork?>() }
    val test = remember { mutableListOf<Artwork?>() }
    Log.d("TEST", test.toString())

    if (viewModel.initialBatchLoaded.observeAsState().value == true) {
        val results = viewModel.artsList.value
        Log.d("RESULTS", results.toString())
        if (results != null) {
            for (i in results.indices) {
                test.add(results[i])
            }
        }
    }

    /**
     * Whole screen
     */
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
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

            OutlinedTextField(
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedIndicatorColor = MaterialTheme.colorScheme.background,
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
                    unfocusedTextColor = MaterialTheme.colorScheme.primary
                ),
                leadingIcon = {
                    IconButton(
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_search_32),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        onClick = {
                            //viewModel.searchArtsWithInput()
                            isLoading = true
                        }
                    )
                },
                modifier = Modifier.padding(start = 10.dp, top = 10.dp),
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
                Text(text = "Filter")
            }
        }

        /**
         * Result area
         */
        HorizontalDivider(modifier = Modifier.shadow(1.dp))

        if (isLoading) {
            Log.d("isLoading", isLoading.toString())
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(all = 20.dp)
                    .width(64.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 8.dp,
                trackColor = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Text(
                modifier = Modifier.padding(all = 10.dp),
                text = if (artworks.isEmpty()) noResultText else resultsText,
                textAlign = TextAlign.Center
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(test) { _, art ->
                    if (art != null) {
                        ArtItem(art = art,
                            modifier = Modifier
                                .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                                .clickable {
                                    /* TODO */
                                }
                                .fillMaxWidth())
                    }
                }
            }
        }
    }
}