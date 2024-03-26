package com.marsu.armuseumproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.marsu.armuseumproject.screens.SelectFromGalleryScreen
import com.marsu.armuseumproject.ui.theme.ARMuseumProjectTheme
import com.marsu.armuseumproject.viewmodels.SelectFromGalleryViewModel

/**
 * Contains SelectFromGalleryScreen composable.
 */
class SelectFromGallery : Fragment() {

    private lateinit var viewmodel: SelectFromGalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewmodel = SelectFromGalleryViewModel()
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ARMuseumProjectTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        SelectFromGalleryScreen(viewModel = viewmodel)
                    }
                }
            }
        }
    }
}