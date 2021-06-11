package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    // Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insert(election: Election)

    // Add select all election query
    @Query("SELECT * from election_table" )
    suspend fun getElections(): List<Election>

    // Add select single election query

    @Query("SELECT * from election_table WHERE id = :electionId")
    suspend fun getElectionWithId(electionId: String): Election?

    // Add delete query
    @Delete
    suspend fun deleteElection(election: Election)

    // Add clear query
    @Query("DELETE FROM election_table")
    suspend fun deleteAllElections()

}