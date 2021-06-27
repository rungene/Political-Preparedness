package com.example.android.politicalpreparedness.election

import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VoterInfoViewModel(private val electionDao: ElectionDao,
                         private val electionID:Int,
                         private val electionDivision: Division

) : ViewModel() {

    // Add live data to hold voter info

    private val _allVoterInfo = MutableLiveData<VoterInfoResponse>()
    val allVoterInfo: LiveData<VoterInfoResponse>
        get() = _allVoterInfo




    // Add var and methods to populate voter info
    init {
        getAllVoterInfo()
    }

    private fun getAllVoterInfo() {
        viewModelScope.launch {
            var theAddress = "country:${electionDivision.country}"
            if (!electionDivision.state.isBlank() && !electionDivision.state.isEmpty()) {
                theAddress += "/state:${electionDivision.state}"
            } else {
                theAddress += "/state:ca"
            }
            _allVoterInfo.value = CivicsApi.retrofitService.getVoterInfoResponse(
                theAddress, electionID)
        }
    }

    // Add var and methods to support loading URLs
    private val _votingLocations = MutableLiveData<String?>()
    val votingLocations: LiveData<String?>
        get() = _votingLocations

    fun onClickVotingLocations() {
        _votingLocations.value = _allVoterInfo.value?.state?.get(0)?.electionAdministrationBody?.votingLocationFinderUrl
    }

    fun navigateToVotingLocations() {
        _votingLocations.value = null
    }

    private val _ballotInfo = MutableLiveData<String?>()
    val ballotInfo: LiveData<String?>
        get() = _ballotInfo

    fun onClickBallotInfo() {
        _votingLocations.value = _allVoterInfo.value?.state?.get(0)?.electionAdministrationBody?.ballotInfoUrl
    }

    fun navigateToBallotInformation() {
        _ballotInfo.value = null
    }




    // Add var and methods to save and remove elections to local database
    // cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */
    private val _isElectionSaved: LiveData<Int>
        get() = electionDao.isElectionSaved(electionID)

    val isFollowedElection =
        Transformations.map(_isElectionSaved) { followValue ->
            followValue?.let {
                followValue == 1
            }
        }

    fun unfollowAndFollowButton() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (isFollowedElection.value == true) {
                    electionDao.electionUnFollow(electionID)
                } else {
                    electionDao.electionFollowed(electionID)
                }
            }
        }
    }


}