package com.logicsquare.parentsmeet.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.logicsquare.parentsmeet.databinding.FragmentMeetBinding
import com.logicsquare.parentsmeet.model.MeetListResponse
import com.logicsquare.parentsmeet.model.MeetRequest
import com.logicsquare.parentsmeet.model.ScheduleMeetRequest
import com.logicsquare.parentsmeet.model.UsersItem
import com.logicsquare.parentsmeet.network.APIClient
import com.logicsquare.parentsmeet.network.APIInterface
import com.logicsquare.parentsmeet.ui.adapter.MeetAdapter
import com.logicsquare.parentsmeet.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MeetFragment : Fragment(), MeetAdapter.OnClickListeners {

    lateinit var binding: FragmentMeetBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getMeetListing()
    }


    private fun getMeetListing() {
        val token = "Bearer ${SharedPref(requireContext()).getToken()}"
        var meetRequest= MeetRequest()
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
        var meetAdapter = MeetAdapter(response.users,this)
        binding.rvMeet.adapter = meetAdapter
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMeetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onClick(usersItem: UsersItem) {

    }

    override fun onMeetClick(usersItem: UsersItem) {

    }

    override fun onMessageClick(usersItem: UsersItem) {

    }

    private fun scheduleMeet() {
        val token = "Bearer ${SharedPref(requireContext()).getToken()}"
        var scheduleMeetRequest= ScheduleMeetRequest("")

        val call: Call<MeetListResponse?> =
            APIClient.client.create(APIInterface::class.java).scheduleMeet(token, scheduleMeetRequest)
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
}