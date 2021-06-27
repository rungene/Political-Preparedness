package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel: ViewModel() {

    // Establish live data for representatives and address

    private val _theRepresentatives = MutableLiveData<List<Representative>>()
    val theRepresentatives: LiveData<List<Representative>>
        get() = _theRepresentatives

    private val _theAddress = MutableLiveData<Address>()
    val theAddress: LiveData<Address>
        get() = _theAddress

    private val _showSnackBar = MutableLiveData<Boolean>()
    val showSnackBar: LiveData<Boolean>
        get() = _showSnackBar


    init {
        _showSnackBar.value = false
    }

    // Create function to fetch representatives from API from a provided address

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */
 fun getMatchingRepresentatives(address:String) {
        viewModelScope.launch {
            val (offices, officials) = CivicsApi.retrofitService.getRepresentativesResponse(address)
            _theRepresentatives.value = offices.flatMap { office ->
                office.getRepresentatives(officials)
            }
        }
    }


    // Create function get address from geo location
    fun getTheAddressFromLocation(address: Address) {
        _showSnackBar.value = false
        _theAddress.value = address

    }

    fun snackBarShown(){
        _showSnackBar.value = false
    }

    // Create function to get address from individual fields

}
