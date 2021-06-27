package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener

class ElectionsFragment: Fragment() {

    // Declare ViewModel
    private lateinit var electionsViewModel: ElectionsViewModel

    private lateinit var upcomingElectionsListAdapter: ElectionListAdapter
    private lateinit var savedElectionsListAdapter: ElectionListAdapter
    private lateinit var fragmentElectionBinding: FragmentElectionBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Add binding values
        fragmentElectionBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_election,
            container,
            false)
        fragmentElectionBinding.lifecycleOwner = this

        // Add ViewModel values and create ViewModel

        val electionsViewModelFactory = ElectionsViewModelFactory(requireActivity().application)
        electionsViewModel = ViewModelProvider(this, electionsViewModelFactory).get(ElectionsViewModel::class.java)
        fragmentElectionBinding.viewModel = electionsViewModel




        // Link elections to voter info
        //here we Observe the navigateToDetailElection LiveData and Navigate when it is not null.

        electionsViewModel.upcomingElections.observe(viewLifecycleOwner, Observer {
            it?.let {
                upcomingElectionsListAdapter.submitList(it)
            }
        })

        electionsViewModel.savedElections.observe(viewLifecycleOwner, Observer {
            it?.let {
                    savedElectionsListAdapter.submitList(it)

            }
        })

        // Initiate recycler adapters

        // Populate recycler adapters

        upcomingElectionsListAdapter = ElectionListAdapter(ElectionListener {
            findNavController().navigate(
                ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(it.id, it.division))
        })
        fragmentElectionBinding.upcomingElectionsRv.adapter = upcomingElectionsListAdapter

        // Setup Recycler View for saved elections
        savedElectionsListAdapter = ElectionListAdapter(ElectionListener {
            findNavController().navigate(
                ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(it.id, it.division))
        })
        fragmentElectionBinding.savedElectionsRv.adapter = savedElectionsListAdapter

        return fragmentElectionBinding.root
    }



    // Refresh adapters when fragment loads
}