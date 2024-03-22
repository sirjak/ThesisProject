package com.marsu.armuseumproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.marsu.armuseumproject.R
import com.marsu.armuseumproject.databinding.FragmentSelectFromGalleryBinding
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
        val binding = DataBindingUtil.inflate<FragmentSelectFromGalleryBinding>(
                inflater, R.layout.fragment_select_from_gallery, container, false
            ).apply {
                composeView.apply {
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
        return binding.root
    }
}