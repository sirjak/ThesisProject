package com.marsu.armuseumproject.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.marsu.armuseumproject.R
import com.marsu.armuseumproject.database.Artwork
import com.marsu.armuseumproject.ui_components.ArtItem
import com.marsu.armuseumproject.viewmodels.ArSelectionViewModel

/**
 * Contains Column with 2 Text composables and a LazyColumn.
 * Text composables display a welcome and when first used, short instructions to using the app.
 * After user has tried some artworks on AR, the instructions change to describing below elements to be recently viewed art.
 * LazyColumn displays 5 artworks that have been tried out in AR most recently.
 */
@Composable
fun HomeScreen(
    lastFive: MutableList<Int>, onNavigate: (Int) -> Unit, viewModel: ArSelectionViewModel
) {
    val welcomeText = stringResource(id = R.string.welcome)
    val introText = stringResource(id = R.string.home_intro)
    val recentText = stringResource(id = R.string.home_recent)

    val artworks = remember { mutableListOf<Artwork?>() }
    for (i in lastFive.indices) {
        viewModel.getArt(lastFive[i]).observeAsState().value.let { artworks.add(it?.get(0)) }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top
    ) {
        Text(
            modifier = Modifier.padding(all = 10.dp),
            style = MaterialTheme.typography.headlineSmall,
            text = welcomeText
        )
        Text(
            modifier = Modifier.padding(all = 10.dp),
            text = if (lastFive.isEmpty()) introText else recentText,
            textAlign = TextAlign.Center
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(artworks) { _, art ->
                if (art != null) {
                    ArtItem(art = art,
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                            .clickable {
                                viewModel.saveId(art.objectID)
                                onNavigate(R.id.navigation)
                            }
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}