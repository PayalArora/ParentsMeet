package com.logicsquare.parentsmeet.ui.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.databinding.FragmentMeetBinding
import com.logicsquare.parentsmeet.databinding.LayoutMeetFilterBinding
import com.logicsquare.parentsmeet.model.*
import com.logicsquare.parentsmeet.network.APIClient
import com.logicsquare.parentsmeet.network.APIInterface
import com.logicsquare.parentsmeet.ui.DrawerActivity
import com.logicsquare.parentsmeet.ui.adapter.MeetAdapter
import com.logicsquare.parentsmeet.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MeetFragment : Fragment(), MeetAdapter.OnClickListeners {

    lateinit var binding: FragmentMeetBinding
    private val LOCATION_PERMISSION_REQ_CODE = 1000;
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double?= null
    private var longitude: Double?= null

    // This will store current location info
    private var currentLocation: Location? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        var meetRequest = MeetRequest()
        getMeetListing(meetRequest)
        checkFilter()
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.menuOption.setOnClickListener {
            startActivity(Intent(requireActivity(),
                DrawerActivity::class.java))
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                // getting the last known or current location
                if (location!= null) {
                latitude = location.latitude
                longitude = location.longitude
                System.out.println("latitude: $latitude")
                System.out.println("longitude: $longitude")
            }

            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed on getting current location",
                    Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkFilter() {
        if (isLocationPermissionGranted())
            getCurrentLocation()
        var bottomSheetBinding: LayoutMeetFilterBinding =
            LayoutMeetFilterBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(bottomSheetBinding.root)
        dialog.show()
        bottomSheetBinding.seekbarLocation.setIntervals(arrayListOf("5", "10", "15", "20"))
        bottomSheetBinding.seekbarRange.setIntervals(arrayListOf("4 Years",
            "5 Years",
            "6 Years",
            "7 Years",
            "8 Years"))
        bottomSheetBinding.abc.setMinSeparationValue(5000F)
        bottomSheetBinding.abc.setValues(1000F, 2000F)
        bottomSheetBinding.showResult.setOnClickListener {
            filterResults(bottomSheetBinding)
        }
        bottomSheetBinding.seekbarLocation.setOnSeekBarChangeListener(object :

            SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(
                seek: SeekBar,

                progress: Int, fromUser: Boolean,
            ) {
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
            }

            override fun onStopTrackingTouch(seek: SeekBar) {

            }

        })

    }


private fun filterResults(bottomSheetDialog: LayoutMeetFilterBinding) {
    var array: ArrayList<String> = arrayListOf()
    var arrayActivies: ArrayList<String> = arrayListOf()
    if (bottomSheetDialog.sixNin.isChecked) {
        array.add(getString(R.string.six_nin))
    }
    if (bottomSheetDialog.ninTwelve.isChecked) {
        array.add(getString(R.string.nin_twelve))
    }
    if (bottomSheetDialog.twelvThree.isChecked) {
        array.add(getString(R.string.twelv_three))
    }
    if (bottomSheetDialog.threeSix.isChecked) {
        array.add(getString(R.string.three_six))
    }
    if (bottomSheetDialog.sixNinPm.isChecked) {
        array.add(getString(R.string.six_nin_pm))
    }
    if (bottomSheetDialog.swim.isChecked) {
        arrayActivies.add(getString(R.string.swim))
    }
    if (bottomSheetDialog.Games.isChecked) {
        arrayActivies.add(getString(R.string.games))
    }
    if (bottomSheetDialog.draw.isChecked) {
        arrayActivies.add(getString(R.string.draw))
    }
    if (bottomSheetDialog.read.isChecked) {
        arrayActivies.add(getString(R.string.read))
    }
    if (bottomSheetDialog.playdate.isChecked) {
        arrayActivies.add(getString(R.string.playdate))
    }
    if (bottomSheetDialog.studyDate.isChecked) {
        arrayActivies.add(getString(R.string.studyDate))
    }
    var meetRequest = MeetRequest()
    meetRequest.filters.preferences.activities = arrayActivies
    meetRequest.filters.preferences.timings = array

    if (latitude!=null && longitude != null) {
        for (i in 1..3) {
            if (bottomSheetDialog.seekbarLocation.progress == i) {
                meetRequest.filters.location.miles = 5 * i
                meetRequest.filters.location.lat = latitude
                meetRequest.filters.location.lng = longitude
            }
        }
    }
    // meetRequest.filters.
    getMeetListing(meetRequest)
}

private fun getMeetListing(meetRequest: MeetRequest) {
    val token = "Bearer ${SharedPref(requireContext()).getToken()}"

    meetRequest.limit = 100

    val call: Call<MeetListResponse?> =
        APIClient.client.create(APIInterface::class.java).getMeetListing(token, meetRequest)
    showProgressBar()
    call.enqueue(object : Callback<MeetListResponse?> {
        override fun onResponse(
            call: Call<MeetListResponse?>,
            response: Response<MeetListResponse?>,
        ) {
            if (response.isSuccessful) {
                if (response.body() != null) {
                    handleResponse(response.body()!!)
                }
            } else {
                handleErrorResponse(response.errorBody(), requireContext())
            }
            hideProgressBar()
        }

        override fun onFailure(call: Call<MeetListResponse?>, t: Throwable) {
            hideProgressBar()
            showToast(t.localizedMessage)
        }
    })
}

private fun handleResponse(response: MeetListResponse) {
    var meetAdapter = MeetAdapter(response.users, this)
    binding.rvMeet.adapter = meetAdapter
}


override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
): View? {
    binding = FragmentMeetBinding.inflate(layoutInflater)
    return binding.root
}

override fun onClick(usersItem: UsersItem) {
    loadFragment(MeetDetailsFragment.newInstance(usersItem))
}

override fun onMeetClick(usersItem: UsersItem) {
    if (SharedPref(requireContext()).getSelectedKid().isNullOrEmpty()) {
        Toast.makeText(requireContext(), "Please select a Kid from the menu", Toast.LENGTH_LONG)
            .show()
        return
    }
    if (!usersItem.kidObject?.id.isNullOrEmpty() && !usersItem.id.isNullOrEmpty()) {
        scheduleMeet(usersItem.kidObject?.id!!, usersItem.id)
    }

}

override fun onMessageClick(usersItem: UsersItem) {

}

private fun scheduleMeet(kidId: String, parentId: String) {
    val token = "Bearer ${SharedPref(requireContext()).getToken()}"
    var scheduleMeetRequest =
        ScheduleMeetRequest(SharedPref(requireContext()).getSelectedKid()!!,
            parentId,
            kidId,
            null,
            null)

    val call: Call<ScheduleMeetResponse?> =
        APIClient.client.create(APIInterface::class.java)
            .scheduleMeet(token, scheduleMeetRequest)
    showProgressBar()
    call.enqueue(object : Callback<ScheduleMeetResponse?> {
        override fun onResponse(
            call: Call<ScheduleMeetResponse?>,
            response: Response<ScheduleMeetResponse?>,
        ) {
            if (response.isSuccessful) {
                if (response.body() != null) {
                    Toast.makeText(requireContext(), "Meet Scheduled", Toast.LENGTH_LONG).show()
                }
            } else {
                handleErrorResponse(response.errorBody(), requireContext())
            }
            hideProgressBar()
        }

        override fun onFailure(call: Call<ScheduleMeetResponse?>, t: Throwable) {
            hideProgressBar()
            showToast(t.localizedMessage)
        }
    })
}

private fun loadFragment(fragment: Fragment) {
    val transaction = requireActivity().supportFragmentManager.beginTransaction()
    transaction.replace(R.id.container, fragment)
    transaction.addToBackStack(fragment.javaClass.name)
    transaction.commit()
}

private fun isLocationPermissionGranted(): Boolean {
    return if (ActivityCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        requestPermissions(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQ_CODE
        )
        false
    } else {
        true
    }
}

override fun onRequestPermissionsResult(
    requestCode: Int, permissions: Array<out String>, grantResults: IntArray,
) {
    when (requestCode) {
        LOCATION_PERMISSION_REQ_CODE -> {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                // permission granted
            } else {
                // permission denied
                Toast.makeText(requireContext(),
                    "You need to grant permission to access location",
                    Toast.LENGTH_SHORT).show()
            }
        }

    }
}
}

