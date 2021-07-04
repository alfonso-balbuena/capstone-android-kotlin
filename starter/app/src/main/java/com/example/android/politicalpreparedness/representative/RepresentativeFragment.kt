package com.example.android.politicalpreparedness.representative

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.android.politicalpreparedness.ElectionApp
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.snackbar.Snackbar
import java.util.Locale
import java.util.jar.Manifest

class DetailFragment : Fragment() {

    companion object {
        private const val PERMISSION_ACCESS_FINE = 1
    }

    private lateinit var binding : FragmentRepresentativeBinding
    private val viewModel : RepresentativeViewModel by viewModels { RepresentativeViewModelFactory(requireActivity().application as ElectionApp) }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_representative,container,false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        initRecyclerView()
        binding.buttonLocation.setOnClickListener {
            getLocation()
        }
        return binding.root
    }

    private fun initRecyclerView() {
        val adapterRepresentative = RepresentativeListAdapter()
        binding.representativesRv.adapter = adapterRepresentative
        viewModel.representative.observe(viewLifecycleOwner, Observer {
            adapterRepresentative.submitList(it)
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == PERMISSION_ACCESS_FINE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            }
            else {
                Snackbar.make(binding.root,R.string.no_granted_permission_location,Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_ACCESS_FINE)
            false
        }
    }

    private fun isPermissionGranted() : Boolean {
        return context?.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if(checkLocationPermissions()) {
            val fusedLocation = LocationServices.getFusedLocationProviderClient(requireActivity())
            fusedLocation.lastLocation.addOnSuccessListener { location ->
                viewModel.getRepresentatives(geoCodeLocation(location))
            }
        }
    }



    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

}