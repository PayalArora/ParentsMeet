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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
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
import com.logicsquare.parentsmeet.ui.adapter.JobFilterAdapter
import com.logicsquare.parentsmeet.ui.adapter.MeetAdapter
import com.logicsquare.parentsmeet.ui.adapter.OnItemClickEvent
import com.logicsquare.parentsmeet.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MeetFragment : Fragment(), MeetAdapter.OnClickListeners, OnItemClickEvent {

    lateinit var binding: FragmentMeetBinding

    var array: ArrayList<String> = arrayListOf()
    var arrayActivies: ArrayList<String> = arrayListOf()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        binding
    }


    private fun checkFilter() {
        var bottomSheetBinding: LayoutMeetFilterBinding =
            LayoutMeetFilterBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(bottomSheetBinding.root)
        dialog.show()
        bottomSheetBinding.seekbarLocation.setIntervals(arrayListOf("5", "10", "15", "20"))

        bottomSheetBinding.seekbarRange.setRange(4F, 8F)

        bottomSheetBinding.showResult.setOnClickListener {
            filterResults(bottomSheetBinding)
        }
        bottomSheetBinding.rvActivities.apply {
            val gridLayoutManager = GridLayoutManager(requireContext(), 3)
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL)
            layoutManager = gridLayoutManager
            if (SettingsFragment.settingsResponse != null) {
                SettingsFragment.settingsResponse?.setting?.preferences?.activities.let {
                    adapter = JobFilterAdapter(0, it as List<Any?>, this@MeetFragment)

                }
            }
        }

        bottomSheetBinding.rvAvailability.apply {
            val gridLayoutManager = GridLayoutManager(requireContext(), 3)
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL)
            layoutManager = gridLayoutManager
            if (SettingsFragment.settingsResponse != null) {
                SettingsFragment.settingsResponse?.setting?.preferences?.timings.let {
                    adapter = JobFilterAdapter(1, it as List<Any?>, this@MeetFragment)

                }
            }
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
        var meetRequest = MeetRequest()
        var filters = MeetRequest.Filters()
        filters.preferences.activities = arrayActivies
        filters.preferences.timings = array

        for (i in 1..3) {
            if (bottomSheetDialog.seekbarLocation.progress == i) {
                filters.location.miles = 5 * i
              //  filters.location.lat = latitude
              //  filters.location.lng = longitude
            }
        }

        filters.age.max = bottomSheetDialog.seekbarRange.maxProgress.toInt()
        filters.age.min = bottomSheetDialog.seekbarRange.minProgress.toInt()
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
        /* if (SharedPref(requireContext()).getSelectedKid().isNullOrEmpty()){
             Toast.makeText(requireContext(),"Please select a Kid from the menu",Toast.LENGTH_LONG).show()
             return
         }
         if (!usersItem.kidObject?.id.isNullOrEmpty() && !usersItem.id.isNullOrEmpty()){
             scheduleMeet(usersItem.kidObject?.id!!,usersItem.id)
         }*/

        loadFragment(MeetDetailsFragment.newInstance(usersItem))

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

    override fun onItemClick(position: ArrayList<String>, type: Int) {
        if (type == 0) {
            arrayActivies.clear()
            arrayActivies.addAll(position)
        } else {
            array.clear()
            array.addAll(position)

        }
    }
}

