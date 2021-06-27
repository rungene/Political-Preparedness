package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    // Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insert(election: Election)

    //inserting all eletions
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun allElectionsInserted(vararg elections: Election)


    // Add select all election query

    @Query("SELECT * from election_table" )
   fun getElections(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table WHERE id in (SELECT id FROM elections_followed_table) ORDER BY electionDay DESC")
    fun getElectionsFollowed(): LiveData<List<Election>>

    @Query("SELECT CASE id WHEN NULL THEN 0 ELSE 1 END FROM elections_followed_table WHERE id = :electionSavedId")
    fun isElectionSaved(electionSavedId: Int): LiveData<Int>

    @Query("INSERT INTO elections_followed_table (id) VALUES(:electionFollowedId)")
    suspend fun electionFollowed(electionFollowedId: Int)
    suspend fun electionFollowed(election: Election){
        electionFollowed(election.id)
    }


    // Add select single election query

    @Query("SELECT * from election_table WHERE id = :electionId")
    suspend fun getElectionWithId(electionId: String): Election?

    // Add delete query
    @Delete
    suspend fun deleteElection(election: Election)

    // Add clear query
    @Query("DELETE FROM election_table")

    suspend fun deleteAllElections()

    @Query("DELETE FROM elections_followed_table WHERE id = :electionUnFollowId")
    suspend fun electionUnFollow(electionUnFollowId: Int)
    suspend fun electionUnFollow(election: Election){
        electionUnFollow(election.id)
    }

}