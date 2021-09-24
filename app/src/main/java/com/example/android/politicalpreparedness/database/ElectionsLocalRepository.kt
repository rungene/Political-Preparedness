package com.example.android.politicalpreparedness.database

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionsLocalRepository(private val electionDatabase: ElectionDatabase
){


    //list of elections tha has been saved
    val elections:LiveData<List<Election>> =electionDatabase.electionDao.getElections()

    // The list of  elections that has been followed.
    // The list of followed elections.
    val electionsFollowed: LiveData<List<Election>> = electionDatabase.electionDao.getElectionsFollowed()



    suspend fun electionsRefreshed() {
        withContext(Dispatchers.IO) {
            try {
                // Get String Json response via Retrofit
                val electionsResponse = CivicsApi.retrofitService.electionResponse()
                val result = electionsResponse.elections

                // Push the results to the database
               electionDatabase.electionDao.allElectionsInserted(*result.toTypedArray())

                Log.d(ContentValues.TAG, result.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}