package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.repository.election.RepositoryElection
import com.example.android.politicalpreparedness.repository.representative.RepositoryRepresentative

class ElectionApp : Application() {

    lateinit var repository : RepositoryElection
    lateinit var repositoryRepresentative: RepositoryRepresentative

    override fun onCreate() {
        super.onCreate()
        val apiService = CivicsApi.retrofitService
        repository = RepositoryElection(ElectionDatabase.getInstance(applicationContext),apiService)
        repositoryRepresentative =  RepositoryRepresentative(apiService)
    }
}