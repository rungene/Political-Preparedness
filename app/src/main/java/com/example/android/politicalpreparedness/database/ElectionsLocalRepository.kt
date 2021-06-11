package com.example.android.politicalpreparedness.database

import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionsLocalRepository(
    private val electionDao: ElectionDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

): ElectionDataSource{
    override suspend fun getElections(): Result<List<Election>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(electionDao.getElections())
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }

    override suspend fun getElectionWithId(id: String): Result<Election> = withContext(ioDispatcher) {
        try {
            val election = electionDao.getElectionWithId(id)
            if (election != null) {
                return@withContext Result.Success(election)
            } else {
                return@withContext Result.Error("Election not found!")
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e.localizedMessage)
        }
    }
    override suspend fun deleteElection(election: Election) {
        withContext(ioDispatcher) {
            electionDao.deleteElection(election)
        }
    }

    override suspend fun deleteAllElections() {
        withContext(ioDispatcher) {
            electionDao.deleteAllElections()
        }
    }


}