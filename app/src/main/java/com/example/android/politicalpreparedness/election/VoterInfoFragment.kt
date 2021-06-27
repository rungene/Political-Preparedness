package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    private lateinit var voterInfoViewModel: VoterInfoViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Add ViewModel values and create ViewModel

        val bundle = VoterInfoFragmentArgs.fromBundle(requireArguments())
        val electionID = bundle.argElectionId
        val electionDivision = bundle.argDivision

        val application = requireNotNull(this.activity).application
        val electionDatabase = ElectionDatabase.getInstance(application).electionDao
        val viewModelFactory = VoterInfoViewModelFactory(electionDatabase, electionID, electionDivision)
        voterInfoViewModel = ViewModelProvider(this, viewModelFactory).get(VoterInfoViewModel::class.java)

        // Add binding values
        val fragmentVoterInfoBinding: FragmentVoterInfoBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_voter_info, container, false)
        fragmentVoterInfoBinding.lifecycleOwner = this
        fragmentVoterInfoBinding.viewModel = voterInfoViewModel

        // Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */
        // Handle loading of URLs
            voterInfoViewModel.votingLocations.observe(viewLifecycleOwner, Observer {
            it?.let {
                loadingURLIntent(it)
                voterInfoViewModel.navigateToVotingLocations()
            }
        })

        voterInfoViewModel.ballotInfo.observe(viewLifecycleOwner, Observer {
            it?.let {
                loadingURLIntent(it)
                voterInfoViewModel.navigateToBallotInformation()
            }
        })



        // Handle save button UI state
        // cont'd Handle save button clicks
        voterInfoViewModel.isFollowedElection.observe(viewLifecycleOwner, Observer { wasElectionFollowed ->
            if (wasElectionFollowed == true) {
                fragmentVoterInfoBinding.saveElectionsButton.text = getString(R.string.unfollow_btn)
            } else {
                fragmentVoterInfoBinding.saveElectionsButton.text = getString(R.string.follow_btn)
            }
        })

        return fragmentVoterInfoBinding.root


    }

    // Create method to load URL intents
    private fun loadingURLIntent(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

}