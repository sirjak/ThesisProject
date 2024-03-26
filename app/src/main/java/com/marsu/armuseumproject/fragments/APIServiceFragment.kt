package com.marsu.armuseumproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.marsu.armuseumproject.screens.ApiServiceScreen
import com.marsu.armuseumproject.ui.theme.ARMuseumProjectTheme
import com.marsu.armuseumproject.viewmodels.ApiServiceViewModel

/**
 * Contains a ApiServiceScreen composable.
 */
class APIServiceFragment : Fragment() {

    private lateinit var viewmodel: ApiServiceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewmodel = ApiServiceViewModel(requireActivity().application)

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ARMuseumProjectTheme {
                    Surface(
                        color = MaterialTheme.colorScheme.background,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        ApiServiceScreen(viewModel = viewmodel)
                    }
                }
            }
        }
    }

    // TODO: Figure out if needed
    /*private fun preventButtonClickSpam(f: () -> Unit) {
        if (SystemClock.elapsedRealtime() - lastDepClick > 1000) {
            lastDepClick = SystemClock.elapsedRealtime()
            f()
        }
    }*/
}