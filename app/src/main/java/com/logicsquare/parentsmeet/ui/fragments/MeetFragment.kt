package com.logicsquare.parentsmeet.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.databinding.FragmentMeetBinding
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getMeetListing()

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.menuOption.setOnClickListener { startActivity(Intent(requireActivity(), DrawerActivity::class.java)) }
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

    private fun scheduleMeet(kidId:String,parentId:String) {
        val token = "Bearer ${SharedPref(requireContext()).getToken()}"
        var scheduleMeetRequest= ScheduleMeetRequest(SharedPref(requireContext()).getSelectedKid()!!,parentId,kidId,null,null)

        val call: Call<ScheduleMeetResponse?> =
            APIClient.client.create(APIInterface::class.java).scheduleMeet(token, scheduleMeetRequest)
        showProgressBar()
        call.enqueue(object : Callback<ScheduleMeetResponse?> {
            override fun onResponse(
                call: Call<ScheduleMeetResponse?>,
                response: Response<ScheduleMeetResponse?>,
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                       Toast.makeText(requireContext(),"Meet Scheduled",Toast.LENGTH_LONG).show()
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
}