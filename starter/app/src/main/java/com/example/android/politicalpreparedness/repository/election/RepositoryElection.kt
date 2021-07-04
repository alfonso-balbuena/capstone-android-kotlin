package com.example.android.politicalpreparedness.repository.election

import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse

class RepositoryElection(private val dataBase : ElectionDatabase,private val apiService: CivicsApiService ) {

    suspend fun getUpcomingElections() : List<Election>  {
        val electionResponse = apiService.getElections()
        return electionResponse.elections
    }

    suspend fun getSaveElections() : List<Election> {
        return dataBase.electionDao.getAll()
    }

    suspend fun saveElection(item : Election) {
        dataBase.electionDao.insert(item)
    }

    suspend fun getVoterInfo(electionId : Long, division : Division) : VoterInfoResponse {
        var address = division.country
        address += if(division.state.isNotEmpty()) " ${division.state}" else " ca"
        return apiService.getVoterInfo(address, electionId)
    }

    suspend fun removeElection(electionId: Int) {
        dataBase.electionDao.deleteElection(electionId)
    }

    suspend fun getElection(electionId: Int) : Election {
        return dataBase.electionDao.getElection(electionId)
    }
}