package com.marsu.armuseumproject.screens

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marsu.armuseumproject.MyApp
import com.marsu.armuseumproject.R
import com.marsu.armuseumproject.database.Artwork
import com.marsu.armuseumproject.database.PreferencesManager
import com.marsu.armuseumproject.ui_components.ArtItem
import com.marsu.armuseumproject.ui_components.ArtPopup
import com.marsu.armuseumproject.ui_components.SelectDepartmentPopup
import com.marsu.armuseumproject.viewmodels.ApiServiceViewModel

@Composable
fun ApiServiceScreen(
    viewModel: ApiServiceViewModel
) {
    val focusManager = LocalFocusManager.current
    val preferencesManager = remember { PreferencesManager(MyApp.appContext) }

    val searchText by viewModel.searchText.collectAsState()
    val isLoading by viewModel.loadingResults.collectAsState()
    val resultsText by viewModel.resultText.collectAsState()
    val noResultText = stringResource(id = R.string.no_result)

    val artworks by viewModel.artsList.observeAsState()
    val initialBatch by viewModel.initialBatchLoaded.collectAsState()

    // Variables associated with ArtPopup
    val showInfo by viewModel.isArtPopupOpen.collectAsState()
    var singleArtwork by remember { mutableStateOf<Artwork?>(null) }

    // Variables associated with SelectDepartmentPopup
    val showDepartments by viewModel.isDepartmentPopupOpen.collectAsState()
    val departmentId by viewModel.departmentID.collectAsState()
    val departmentName by viewModel.departmentName.collectAsState()

    // Retrieve department selection information from shared preferences
    if (!showDepartments) {
        val nameId = preferencesManager.getData("selectedDepartmentName", "")
        val id = preferencesManager.getData("selectedDepartment", "")

        if (nameId !== null && nameId !== "" && id !== null && id !== "") {
            if (departmentId == 0 || departmentId != id.toInt()) {
                viewModel.updateDepartmentID(id.toInt())
                viewModel.updateDepartmentName(stringResource(id = nameId.toInt()))
                viewModel.searchArtsWithInput()
            }
        }
    }

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
            horizontalArrangement = Arrangement.Start,
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

            TextButton(modifier = Modifier.padding(end = 10.dp),
                onClick = { viewModel.onFilterButtonClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_filter_alt_24),
                    contentDescription = null
                )
                Text(text = stringResource(id = R.string.filter))
            }
        }

        // Filter tag
        if (departmentId != 0) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .fillMaxWidth()
                    .padding(start = 10.dp, bottom = 10.dp)
            ) {
                OutlinedTextField(colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
                ),
                    leadingIcon = {
                        IconButton(content = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_close_24),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }, onClick = {
                            preferencesManager.saveData("selectedDepartment", "")
                            preferencesManager.saveData("selectedDepartmentName", "")
                            viewModel.resetSelectedDepartment()
                            launchSearch()
                        })
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .wrapContentHeight(Alignment.CenterVertically),
                    readOnly = true,
                    shape = MaterialTheme.shapes.extraLarge,
                    textStyle = TextStyle(fontSize = 14.sp),
                    value = departmentName,
                    onValueChange = {})
            }
        }
        /**
         * Result area
         */
        HorizontalDivider(modifier = Modifier.shadow(1.dp))

        if (isLoading && !initialBatch) {
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
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(all = 10.dp),
                text = (if (resultsText !== "") resultsText else noResultText),
                textAlign = TextAlign.Center
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                if (!artworks.isNullOrEmpty()) {
                    itemsIndexed(artworks!!) { index, art ->
                        ArtItem(art = art,
                            modifier = Modifier
                                .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                                .clickable {
                                    singleArtwork = art
                                    viewModel.onArtItemClick()
                                }
                                .fillMaxWidth())

                        // Loading more items when getting closer to the bottom
                        if (index >= artworks!!.size - 3) {
                            if ((!isLoading)) {
                                viewModel.getArts(false)
                            }
                        }
                    }
                }
            }
        }
        if (showInfo && singleArtwork !== null) {
            ArtPopup(art = singleArtwork!!, onDismiss = { viewModel.onDismissPopup() })
        }
        if (showDepartments) {
            SelectDepartmentPopup(onDismiss = { viewModel.onDismissPopup() })
        }
    }
}