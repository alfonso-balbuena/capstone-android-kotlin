package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.ElectionApp
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener


class ElectionsFragment: Fragment() {

    private val viewModel : ElectionsViewModel by viewModels { ElectionsViewModelFactory(requireActivity().application as ElectionApp) }
    private lateinit var binding : FragmentElectionBinding


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_election,container,false)
        binding.viewModel = viewModel
        viewModel.resetNavigation()
        viewModel.navigate.observe(viewLifecycleOwner, Observer { navDirections ->
            navDirections?.let {
                findNavController().navigate(it)
            }
        })
        initRecyclers()
        return binding.root
    }

    private fun initRecyclers() {
        val listener = ElectionListener{
            viewModel.navigateToVoter(it)
        }

        val adapterUpcoming = ElectionListAdapter(listener)
        viewModel.upcomingElections.observe(viewLifecycleOwner, Observer {
            adapterUpcoming.submitList(it)
        })
        binding.upcomingRecyclerView.adapter = adapterUpcoming
        val adapterSaved = ElectionListAdapter(listener)
        viewModel.savedElections.observe(viewLifecycleOwner, Observer {
            adapterSaved.submitList(it)
        })
        binding.savedRecyclerView.adapter = adapterSaved

    }

    override fun onStart() {
        super.onStart()
        viewModel.getSavedElections()
        viewModel.getUpcomingElections()
    }
}