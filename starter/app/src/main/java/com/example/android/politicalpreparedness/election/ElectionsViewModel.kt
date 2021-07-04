package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.election.RepositoryElection
import kotlinx.coroutines.launch


class ElectionsViewModel(private val repository : RepositoryElection): ViewModel() {

    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections : LiveData<List<Election>>
        get() = _upcomingElections

    private val _savedElections = MutableLiveData<List<Election>>()
    val savedElections : LiveData<List<Election>>
        get() = _savedElections

    private val _navigate = MutableLiveData<NavDirections?>()
    val navigate : LiveData<NavDirections?>
    get() = _navigate

    fun getUpcomingElections() {
        viewModelScope.launch {
            _upcomingElections.value = repository.getUpcomingElections()
        }
    }

    fun getSavedElections() {
        viewModelScope.launch {
            _savedElections.value = repository.getSaveElections()
        }
    }

    fun navigateToVoter(selectedElection : Election) {
        _navigate.value = ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(selectedElection.id,selectedElection.division)
    }

    fun resetNavigation() {
        _navigate.value = null
    }
}