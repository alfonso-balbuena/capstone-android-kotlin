package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.ElectionApp

class RepresentativeViewModelFactory (private val app : ElectionApp): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RepresentativeViewModel::class.java)) {
            return RepresentativeViewModel(app.repositoryRepresentative,app) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}