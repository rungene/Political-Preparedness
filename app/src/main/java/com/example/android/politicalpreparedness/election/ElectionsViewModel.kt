package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.database.ElectionsLocalRepository
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch

// Construct ViewModel and provide election datasource
class ElectionsViewModel(application: Application): ViewModel() {

    //database
    private val database = ElectionDatabase.getInstance(application)

    //the repository
    private val electionsLocalRepository =ElectionsLocalRepository(database)


    // Create live data val for upcoming elections
    val upcomingElections: LiveData<List<Election>>
        get() = electionsLocalRepository.elections


    // Create live data val for saved elections
    val savedElections: LiveData<List<Election>>
        get() = electionsLocalRepository.electionsFollowed


    // Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
    //init block
    init {
        viewModelScope.launch {
            electionsLocalRepository.electionsRefreshed()
        }
    }

    // Create functions to navigate to saved or upcoming election voter info

    private val _moveToSelectedDetailsElection = MutableLiveData<Election>()
    val navigateToSelectedUpcomingElection: LiveData<Election>
        get() = _moveToSelectedDetailsElection


    fun electionDetails(election: Election) {
        _moveToSelectedDetailsElection.value = election
    }

    fun completeElection() {
        _moveToSelectedDetailsElection.value = null
    }
}