package com.marsu.armuseumproject.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberNodes

@Composable
fun ArScreen() {
    val engine = rememberEngine()
    val cameraNode = rememberARCameraNode(engine = engine)
    val childNodes = rememberNodes()
    ARScene(
        modifier = Modifier.fillMaxSize(),
        childNodes = childNodes,
        planeRenderer = true
    )
}
