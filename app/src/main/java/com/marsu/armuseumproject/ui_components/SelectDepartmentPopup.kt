package com.marsu.armuseumproject.ui_components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog

@Composable
fun SelectDepartmentPopup(onDismiss: () -> Unit) {
    // TODO: Add all the elements and functionality
    Dialog(
        content = {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.90f)
                    .fillMaxHeight(0.80f),
                shape = MaterialTheme.shapes.large
            ) {
                Text(text = "Testing testing woop woop birdie poop")
            }
        },
        onDismissRequest = { onDismiss() })
}