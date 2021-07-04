package com.example.android.politicalpreparedness.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Insert
    suspend fun insert(election : Election)

    @Query("SELECT * FROM election_table")
    suspend fun getAll() : List<Election>

    @Query("SELECT * FROM election_table WHERE id = :id")
    suspend fun getElection(id : Int) : Election

    @Query("DELETE FROM election_table WHERE id = :id")
    suspend fun deleteElection(id : Int)

    @Query("DELETE FROM election_table")
    suspend fun deleteAll()
}