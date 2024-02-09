package com.marsu.armuseumproject.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.marsu.armuseumproject.R
import com.marsu.armuseumproject.ui.theme.ARMuseumProjectTheme

class ArSelectionScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ARMuseumProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ArSelectionScreen("Android")
                }
            }
        }
    }
}

@Composable
fun ArSelectionScreen(name: String, modifier: Modifier = Modifier) {
    LazyColumn() {
        item {
            //ListItem(art = )
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = stringResource(id = R.string.chosen_artwork),
            modifier = modifier
        )
        Text(text = stringResource(id = R.string.none))
        Button(onClick = { /*TODO*/ }) {
            Text(text = stringResource(id = R.string.start_ar))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ARMuseumProjectTheme {
        ArSelectionScreen("Android")
    }
}