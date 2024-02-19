package com.marsu.armuseumproject.fragments

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.marsu.armuseumproject.R
import com.marsu.armuseumproject.databinding.FragmentApiServiceBinding
import com.marsu.armuseumproject.screens.ApiServiceScreen
import com.marsu.armuseumproject.ui.theme.ARMuseumProjectTheme
import com.marsu.armuseumproject.viewmodels.ApiServiceViewModel

/**
 * Contains an EditText and RecyclerView for fetching and displaying found artwork from the API, as well as a button for opening up
 * the SelectDepartmentActivity for filtering the found Artwork objects.
 */
class APIServiceFragment : Fragment() {

    private lateinit var viewmodel: ApiServiceViewModel
    private var lastDepClick = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewmodel = ApiServiceViewModel(requireActivity().application)
        val binding = DataBindingUtil.inflate<FragmentApiServiceBinding>(
            inflater,
            R.layout.fragment_api_service,
            container,
            false
        ).apply {
            composeView.apply {
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
        // Recyclerview setup
        /*adapter = ApiServiceAdapter()
        adapter.setHasStableIds(true)
        layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = layoutManager*/

        // Department settings
        /*binding.openDepartmentSettings.setOnClickListener {
            preventButtonClickSpam {
                val intent = Intent(activity, SelectDepartmentActivity::class.java)
                startActivity(intent)
            }
        }*/

        // Clear button for department
        /*binding.resetDepartment.setOnClickListener {
            apiServiceViewModel.resetSelectedDepartment()
        }*/

        /*apiServiceViewModel.departmentId.observe(viewLifecycleOwner) {
            it.let {
                if (it == 0) {
                    binding.departmentIndicator.visibility = View.GONE
                } else {
                    binding.departmentIndicator.visibility = View.VISIBLE
                }
            }
        }*/

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        //apiServiceViewModel.updateDepartmentID()
    }

    private fun preventButtonClickSpam(f: () -> Unit) {
        if (SystemClock.elapsedRealtime() - lastDepClick > 1000) {
            lastDepClick = SystemClock.elapsedRealtime()
            f()
        }
    }
}