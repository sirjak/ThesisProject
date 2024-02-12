package com.marsu.armuseumproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.marsu.armuseumproject.MyApp
import com.marsu.armuseumproject.R
import com.marsu.armuseumproject.database.PreferencesManager
import com.marsu.armuseumproject.databinding.FragmentArSelectionBinding
import com.marsu.armuseumproject.screens.ArSelectionScreen
import com.marsu.armuseumproject.viewmodels.ArSelectionViewModel
import java.lang.reflect.Type

const val SHARED_KEY = "LAST_FIVE"

/**
 * Fragment for selecting artwork to be displayed in AR mode. Displays all the artwork saved to the Room database
 * in a RecyclerView. When an artwork has been selected, the 'Start AR' button can be used to navigate to the AR
 * activity with the image uri coming along as a navigation argument. Also saves the artwork to the most recent
 * artworks when navigating to AR mode.
 */

class ArSelection : Fragment() {

    private lateinit var arSelectionViewModel: ArSelectionViewModel
    private var lastFive = mutableListOf<Int>() // initiate variable

    private val args: ArSelectionArgs by navArgs()

    // Handle possible navigation arguments if coming via the recent artworks list from Home Fragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chosenArtwork = args.latestArtwork
        /*if (chosenArtwork != null) {
            binding.chosenTitle.text = chosenArtwork.title
            binding.chosenArtist.text = chosenArtwork.artistDisplayName
            arSelectionViewModel.imageUri.postValue(chosenArtwork.primaryImage.toUri())
            arSelectionViewModel.imageId.postValue(chosenArtwork.objectID)
        }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arSelectionViewModel = ArSelectionViewModel(requireActivity().application)

        // Retrieving the previously stored list of id's to use it as a base for lastFive
        val preferences = PreferencesManager(MyApp.appContext)
        val json = preferences.getData(SHARED_KEY, null)
        val type: Type = object : TypeToken<List<Int>>() {}.type
        if (json != null) {
            lastFive = Gson().fromJson(json, type)
        }

        val binding = DataBindingUtil.inflate<FragmentArSelectionBinding>(
            inflater,
            R.layout.fragment_ar_selection,
            container,
            false
        ).apply {
            composeView.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    MaterialTheme {
                        ArSelectionScreen(
                            lastFive = lastFive,
                            viewModel = arSelectionViewModel
                        )
                    }
                }
            }
        }

        return binding.root
    }
}
