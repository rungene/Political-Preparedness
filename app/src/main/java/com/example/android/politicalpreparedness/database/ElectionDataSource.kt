package com.example.android.politicalpreparedness.database

import com.example.android.politicalpreparedness.network.models.Election

interface ElectionDataSource {
    suspend fun getElections(): Result<List<Election>>
    suspend fun getElectionWithId(id: String): Result<Election>
    suspend fun deleteElection(election: Election)
    suspend fun deleteAllElections()



}