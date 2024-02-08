package com.marsu.armuseumproject.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.marsu.armuseumproject.R
import com.marsu.armuseumproject.adapters.HomeRecyclerAdapter
import com.marsu.armuseumproject.database.Artwork
import com.marsu.armuseumproject.databinding.FragmentHomeBinding
import com.marsu.armuseumproject.ui_components.ListItem
import com.marsu.armuseumproject.viewmodels.HomeViewModel
import java.lang.reflect.Type

/**
 * Contains 2 TextViews and RecyclerView.
 * TextViews display a welcome and when first used, short instructions to using the app.
 * After user has tried some artworks on AR, the instructions change to describing below elements to be recently viewed art.
 * RecyclerView displays 5 artworks that have been tried out in AR most recently.
 */
class HomeFragment : Fragment() {

    private lateinit var adapter: HomeRecyclerAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var viewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var lastFive = mutableListOf<Int>() // initiate variable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = HomeViewModel(requireActivity().application)
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        /*val binding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater,
            R.layout.fragment_home,
            container,
            false
        ).apply {
            composeView.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    MaterialTheme {
                        HomeScreen(viewModel = viewModel)
                    }
                }
            }
        }*/
        val view = binding.root

        adapter = HomeRecyclerAdapter()
        adapter.setHasStableIds(true)

        // When clicking on recent artworks, navigate to the AR selection fragment with the chosen artwork being automatically selected there
        adapter.onItemClick = { artwork ->
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToArSelection(
                    artwork
                )
            )
        }

        layoutManager = LinearLayoutManager(activity)
        binding.homeRecycler.adapter = adapter
        binding.homeRecycler.setHasFixedSize(true)
        binding.homeRecycler.layoutManager = layoutManager

        // Retrieve lastFive from shared preferences and converting back to list from json
        val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)
        val json = sharedPreferences?.getString(SHARED_KEY, null)
        val type: Type = object : TypeToken<List<Int>>() {}.type
        if (json != null) {
            lastFive = Gson().fromJson(json, type)
        }

        // Checking if lastFive has anything in it
        // If not, showing instructions text instead of most recent artworks.
        if (lastFive.isEmpty()) {
            binding.homeIntro.text = getString(R.string.home_intro)
        }

        // Getting the artwork objects by id's collected in lastFive and sending info to recycler adapter
        val collectedLastFive: MutableList<Artwork> = mutableListOf()
        for (i in lastFive.indices) {
            viewModel.getArt(lastFive[i]).observe(viewLifecycleOwner) { item ->
                item.let {
                    if (it != null) {
                        collectedLastFive += it
                    }
                    adapter.setData(collectedLastFive)
                }
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //return binding.root
}


@Composable
fun HomeScreen(viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val introText = stringResource(id = R.string.home_intro)
    val recentText = stringResource(id = R.string.home_recent)
    var infoText by remember { mutableStateOf(introText) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            fontSize = 24.sp,
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 16.dp),
            text = stringResource(id = R.string.welcome)
        )
        Text(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 30.dp),
            textAlign = TextAlign.Center,
            text = infoText
        )
        if (infoText == recentText) {
            LazyColumn(modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 10.dp)) {
                item {
                    //ListItem(art =)
                }
            }
        }
    }
}