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
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.marsu.armuseumproject.MyApp
import com.marsu.armuseumproject.R
import com.marsu.armuseumproject.database.PreferencesManager
import com.marsu.armuseumproject.databinding.FragmentHomeBinding
import com.marsu.armuseumproject.screens.HomeScreen
import com.marsu.armuseumproject.ui.theme.ARMuseumProjectTheme
import com.marsu.armuseumproject.viewmodels.HomeViewModel
import java.lang.reflect.Type

/**
 * Contains 2 TextViews and RecyclerView.
 * TextViews display a welcome and when first used, short instructions to using the app.
 * After user has tried some artworks on AR, the instructions change to describing below elements to be recently viewed art.
 * RecyclerView displays 5 artworks that have been tried out in AR most recently.
 */
class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private var lastFive = mutableListOf<Int>() // initiate variable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = HomeViewModel(requireActivity().application)

        // Retrieving the previously stored list of id's to use it as a base for lastFive
        val preferences = PreferencesManager(MyApp.appContext)
        val json = preferences.getData(SHARED_KEY, null)
        val type: Type = object : TypeToken<List<Int>>() {}.type
        if (json != null) {
            lastFive = Gson().fromJson(json, type)
        }

        // TODO: Implement remembering which ArtItem was clicked in Home and preselect it at ArScreen
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater,
            R.layout.fragment_home,
            container,
            false
        ).apply {
            composeView.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    ARMuseumProjectTheme {
                        Surface(modifier = Modifier.fillMaxSize()) {
                            HomeScreen(
                                lastFive = lastFive,
                                onNavigate = { _ ->
                                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToArSelection())
                                },
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
        return binding.root
    }
    // TODO: Figure out if what this part does and if it should be kept
    /*override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }*/
}