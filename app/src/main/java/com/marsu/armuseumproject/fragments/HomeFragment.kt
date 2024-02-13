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

    //private var _binding: FragmentHomeBinding? = null
    //private val binding get() = _binding!!
    //private lateinit var adapter: HomeRecyclerAdapter
    //private lateinit var layoutManager: LinearLayoutManager

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
                            HomeScreen(lastFive = lastFive, viewModel = viewModel)
                        }
                    }
                }
            }
        }
        // Inflate the layout for this fragment
        //_binding = FragmentHomeBinding.inflate(inflater, container, false)
        //val view = binding.root

        //adapter = HomeRecyclerAdapter()
        //adapter.setHasStableIds(true)

        // When clicking on recent artworks, navigate to the AR selection fragment with the chosen artwork being automatically selected there
        /*adapter.onItemClick = { artwork ->
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToArSelection(artwork))
        }*/

        /*layoutManager = LinearLayoutManager(activity)
        binding.homeRecycler.adapter = adapter
        binding.homeRecycler.setHasFixedSize(true)
        binding.homeRecycler.layoutManager = layoutManager*/

        // Retrieve lastFive from shared preferences and converting back to list from json
        /*val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)
        val json = sharedPreferences?.getString(SHARED_KEY, null)
        val type: Type = object : TypeToken<List<Int>>() {}.type
        if (json != null) {
            lastFive = Gson().fromJson(json, type)
        }*/

        // Checking if lastFive has anything in it
        // If not, showing instructions text instead of most recent artworks.
        /*if (lastFive.isEmpty()) {
            binding.homeIntro.text = getString(R.string.home_intro)
        }*/

        // Getting the artwork objects by id's collected in lastFive and sending info to recycler adapter
        /*val collectedLastFive: MutableList<Artwork> = mutableListOf()
        for (i in lastFive.indices) {
            viewModel.getArt(lastFive[i]).observe(viewLifecycleOwner) { item ->
                item.let {
                    if (it != null) {
                        collectedLastFive += it
                    }
                    adapter.setData(collectedLastFive)
                }
            }
        }*/

        return binding.root
    }

    /*override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }*/
}