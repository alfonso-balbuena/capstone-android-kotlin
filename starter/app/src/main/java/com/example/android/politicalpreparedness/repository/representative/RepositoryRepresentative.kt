package com.example.android.politicalpreparedness.repository.representative

import android.util.Log
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse

class RepositoryRepresentative(private val apiService: CivicsApiService) {

    suspend fun getRepresentatives(address : String) : RepresentativeResponse {
        val aux = apiService.getRepresentativeInfoByAddress(address)
        Log.d("REPOSITORY","$aux")
        return aux
    }
}