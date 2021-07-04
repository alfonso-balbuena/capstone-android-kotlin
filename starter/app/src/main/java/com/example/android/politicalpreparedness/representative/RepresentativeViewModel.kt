package com.example.android.politicalpreparedness.representative

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.repository.representative.RepositoryRepresentative
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(private val repository : RepositoryRepresentative,private val context: Application): ViewModel() {

    val address_1 = MutableLiveData<String>()
    val address_2 = MutableLiveData<String>()
    val city = MutableLiveData<String>()
    val zip = MutableLiveData<String>()
    val itemPosition = MutableLiveData<Int>().apply {
        value = 0
    }

    private val _representatives = MutableLiveData<List<Representative>>()
    val representative : LiveData<List<Representative>>
    get() = _representatives

    fun getRepresentatives() {
        viewModelScope.launch {
            val (offices,officials) = repository.getRepresentatives(getAddress())
            _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }
        }
    }

    fun getRepresentatives(address: Address) {
        address_1.value = address.line1
        address_2.value = address.line2
        city.value = address.city
        zip.value = address.zip
        val states = context.resources.getStringArray(R.array.states_array)
        itemPosition.value = states.indexOf(address.state)
        getRepresentatives()
    }

    private fun getAddress() : String {
        val states = context.resources.getStringArray(R.array.states_array)
        return "${address_1.value} ${address_2.value} ${zip.value} ${city.value} ${states[itemPosition.value!!]}"
    }
}
