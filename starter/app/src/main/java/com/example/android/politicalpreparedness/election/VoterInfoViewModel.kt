package com.example.android.politicalpreparedness.election

import androidx.arch.core.util.Function
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.State
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.election.RepositoryElection
import kotlinx.coroutines.launch

class VoterInfoViewModel(private val dataSource: RepositoryElection) : ViewModel() {

    companion object {
        private val FOLLOW = "FOLLOW ELECTION"
        private val UNFOLLOW = "UNFOLLOW ELECTION"
    }

    private val _electionDataBase = MutableLiveData<Election>()

    val stateButton : LiveData<String> = Transformations.map(_electionDataBase, Function { election ->
        if (election == null) FOLLOW else UNFOLLOW
    })

    private val _voteInfo = MutableLiveData<VoterInfoResponse>()
    val voteInfo : LiveData<VoterInfoResponse>
        get() = _voteInfo

    private val _votingURL = MutableLiveData<String?>()
    val votingURL : LiveData<String?>
    get() = _votingURL

    private val _ballotURL = MutableLiveData<String?>()
    val ballotURL : LiveData<String?>
    get() = _ballotURL

    val stringVotingURL : LiveData<String> = Transformations.map(_voteInfo, Function { vote ->
        if (hasUrl(vote)) "Voting locations" else ""
    })

    val stringBallotURL : LiveData<String> = Transformations.map(_voteInfo, Function { vote ->
        if (hasUrl(vote)) "Ballot information" else ""
    })

    private val _areDataValid = MutableLiveData<Boolean>()
    val areDataValid : LiveData<Boolean>
    get() = _areDataValid

    fun getVoteInfo(electionId: Long,div : Division) {
        viewModelScope.launch {
            _voteInfo.value = dataSource.getVoterInfo(electionId,div)
            updateElectionDataBase(electionId.toInt())

        }
    }

    fun reset() {
        _votingURL.value = null
        _ballotURL.value = null
    }

    private fun updateElectionDataBase(electionId : Int) {
        viewModelScope.launch {
            _electionDataBase.value = dataSource.getElection(electionId)
        }
    }


    fun votingNavigationURL() {
        gettingURL {
            _votingURL.value = it.electionAdministrationBody.votingLocationFinderUrl
        }
    }

    fun votingNavigationBallot() {
        gettingURL {
            _ballotURL.value = it.electionAdministrationBody.ballotInfoUrl
        }
    }

    private fun hasUrl(vote : VoterInfoResponse) : Boolean {
        return vote.state != null && vote.state.isNotEmpty()
    }

    private fun gettingURL(listener: (state : State) -> Unit) {
        _voteInfo.value?.let {voterInfoResponse ->
            voterInfoResponse.state?.let { list ->
                if(list.isNotEmpty()) {
                    listener(list[0])
                }
            }
        }
    }

    fun deleteElection(electionId: Int) {
        viewModelScope.launch {
            dataSource.removeElection(electionId)
        }
    }

    fun saveElection(election : Election) {
        viewModelScope.launch {
            dataSource.saveElection(election)
        }
    }

    fun saveOrDeleteElection(election : Election) {
        if(stateButton.value == FOLLOW) {
            saveElection(election)
        }
        else {
            deleteElection(election.id)
        }
        updateElectionDataBase(election.id)
    }

    fun validateData(electionId : Int, division : Division) {
        _areDataValid.value = electionId > 0 && division.country.isNotEmpty()
    }
}