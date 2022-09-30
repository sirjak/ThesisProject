package com.marsu.armuseumproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.marsu.armuseumproject.R
import com.marsu.armuseumproject.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.arSelectionNavigation.setOnClickListener { view.findNavController().navigate(R.id.action_homeFragment_to_ar_Selection) }
        binding.openApiService.setOnClickListener { view.findNavController().navigate(R.id.action_homeFragment_to_APIServiceFragment) }
        binding.openSFG.setOnClickListener { view.findNavController().navigate(R.id.action_homeFragment_to_selectFromGallery) }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}