package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.ElectionApp

class ElectionsViewModelFactory(private val app: ElectionApp): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ElectionsViewModel::class.java)) {
            return ElectionsViewModel(app.repository) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}