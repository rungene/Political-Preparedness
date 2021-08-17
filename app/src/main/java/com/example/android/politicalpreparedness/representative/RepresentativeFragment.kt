package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.adapter.setNewValue
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

class DetailFragment : Fragment() {

    companion object {
        // Add Constant for Location request
        private const val LOCATION_PERMISSION_REQUEST = 1
    }

    //Declare ViewModel

    private val representativeViewModel:RepresentativeViewModel by lazy{
        ViewModelProvider(this).get(RepresentativeViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Establish bindings

        val fragmentRepresentativeBinding = FragmentRepresentativeBinding.inflate(inflater)
        fragmentRepresentativeBinding.lifecycleOwner=this
        fragmentRepresentativeBinding.viewmodel=representativeViewModel


        // Define and assign Representative adapter
        val representativeListAdapter =RepresentativeListAdapter()
        fragmentRepresentativeBinding.representativesRv.adapter=representativeListAdapter

        // Populate Representative adapter
        representativeViewModel.theRepresentatives.observe(viewLifecycleOwner, Observer {
            it?.let {
                representativeListAdapter.submitList(it)
            }
        })

        representativeViewModel.theAddress.observe(viewLifecycleOwner, Observer {
            it?.let {
                fragmentRepresentativeBinding.state.setNewValue(it.state)
            }
        })




        // Establish button listeners for field and location search
        fragmentRepresentativeBinding.buttonSearch.setOnClickListener {
            representativeViewModel.getMatchingRepresentatives(representativeViewModel.theAddress.value.toString())
            hideKeyboard()
        }

        fragmentRepresentativeBinding.buttonLocation.setOnClickListener {
            getLocation()
        }


        return fragmentRepresentativeBinding.root

    }
    //checking for permission before making the call to the concerned API.
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray) {
        if (isForegroundPermissionEnabled()) {
            getLocation()
        }else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                Snackbar.make(requireView(), getString(R.string.access_location_needed), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.enable_location)) {
                        requestLocationPermission()
                    }
                    .show()
            } else {
                Snackbar.make(requireView(), getString(R.string.location_permission_needed), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.change_permissions)) {
                        startActivity(Intent().apply {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            data = Uri.fromParts("package",BuildConfig.APPLICATION_ID, null)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        })
                    }
                    .show()
            }
        }


    }
    fun isForegroundPermissionEnabled(): Boolean {
        return (PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION))
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST
        )
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            // Request Location permissions
                requestLocationPermission()
            false
        }
    }

    private fun isPermissionGranted() : Boolean {
        //: Check if permission is already granted and return (true = granted, false = denied/other)
        return ContextCompat.checkSelfPermission(
            requireContext(),
            ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED


    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        // Get location from LocationServices
        // The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
        if (isForegroundPermissionEnabled()) {
            LocationServices.getFusedLocationProviderClient(requireContext())
                .lastLocation.addOnSuccessListener { location ->
                    representativeViewModel.getTheAddressFromLocation(geoCodeLocation(location))
                    representativeViewModel.getMatchingRepresentatives(representativeViewModel.theAddress.value.toString())
                }
        }
        else {
            // Request location permission
           requestLocationPermission()
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
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

}