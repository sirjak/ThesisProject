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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.marsu.armuseumproject.MyApp
import com.marsu.armuseumproject.database.PreferencesManager
import com.marsu.armuseumproject.database.SHARED_KEY
import com.marsu.armuseumproject.screens.HomeScreen
import com.marsu.armuseumproject.ui.theme.ARMuseumProjectTheme
import com.marsu.armuseumproject.viewmodels.ArSelectionViewModel
import java.lang.reflect.Type

/**
 * Contains HomeScreen composable
 */
class HomeFragment : Fragment() {

    private var lastFive = mutableListOf<Int>() // initiate variable
    private val viewModel: ArSelectionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        // Retrieving the previously stored list of id's to use it as a base for lastFive
        val preferences = PreferencesManager(MyApp.appContext)
        val json = preferences.getData(SHARED_KEY, null)
        val type: Type = object : TypeToken<List<Int>>() {}.type
        if (json != null) {
            lastFive = Gson().fromJson(json, type)
        }

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ARMuseumProjectTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        HomeScreen(
                            lastFive = lastFive, onNavigate = { _ ->
                                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToArSelection())
                            }, viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}
