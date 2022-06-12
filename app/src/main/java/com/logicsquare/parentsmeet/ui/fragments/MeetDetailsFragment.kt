package com.logicsquare.parentsmeet.ui.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.chip.ChipGroup
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.databinding.FragmentMeetDetailsBinding
import com.logicsquare.parentsmeet.model.ScheduleMeetRequest
import com.logicsquare.parentsmeet.model.ScheduleMeetResponse
import com.logicsquare.parentsmeet.model.UsersItem
import com.logicsquare.parentsmeet.network.APIClient
import com.logicsquare.parentsmeet.network.APIInterface
import com.logicsquare.parentsmeet.ui.fragments.SettingsFragment.ParentInterent.settingsResponse
import com.logicsquare.parentsmeet.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class MeetDetailsFragment : Fragment() {

    lateinit var binding: FragmentMeetDetailsBinding
    lateinit var userData: UsersItem
    var activitiesMainList = arrayListOf<String>()
    var selectedActivity = ""

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvParentName.text = "${userData.name?.first} ${userData.name?.last}"
        binding.tvName.text = "meet ${userData.name?.first} ${userData.name?.last}"

        if (settingsResponse != null) {
            settingsResponse?.setting?.preferences?.activities.let {
                activitiesMainList.add("Search")
                activitiesMainList.addAll(it as ArrayList<String>)
//                activitiesMainList.add("Other")

                setSpinnerAdapter()
            }
        }
        var parentInterests: String = ""

        if (!userData.preferences?.parentInterests.isNullOrEmpty())
            for (item in userData.preferences?.parentInterests!!) {
                parentInterests = "$parentInterests, ${item.capitalize()} ,"
            }

        parentInterests = parentInterests.removePrefix(",")
        parentInterests = parentInterests.removeSuffix(",")

        var childInterests: String = ""

        if (!userData.kidObject?.preferences?.activities.isNullOrEmpty())
            for (item in userData.kidObject?.preferences?.activities!!) {
                childInterests = "$childInterests,${item.capitalize()} ,"
            }

        if (!userData.kidObject?.preferences?.games.isNullOrEmpty())
            for (item in userData.kidObject?.preferences?.games!!) {
                childInterests = "$childInterests, ${item.capitalize()} ,"
            }

        childInterests = childInterests.removePrefix(",")
        childInterests = childInterests.removeSuffix(",")


        binding.tvParentLocation.text = userData.phoneCountryCode
        binding.tvParentInterests.text = parentInterests

        binding.tvChildName.text = userData.kidObject?.name
        binding.tvChildAge.text = userData.kidObject?.age.toString()

        var timings: String = ""

        if (!userData.kidObject?.preferences?.timings.isNullOrEmpty())
            for (item in userData.kidObject?.preferences?.timings!!) {
                timings = "$timings, ${item.capitalize()} ,"
            }

        timings = timings.removePrefix(",")
        timings = timings.removeSuffix(",")

        binding.tvChildInterests.text = childInterests
        binding.tvMeetingAvailability.text = timings



        binding.btnMeet.setOnClickListener {
            if (SharedPref(requireContext()).getSelectedKid().isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please select a Kid from the menu",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            val time: String = binding.edtTime.text.toString().ifEmpty { "00:00" }
            Log.e("time= ", "$time")
            var date: Date? = validateDate("${binding.edtCalender.text.toString()} $time}")
            if (binding.edtCalender.text.toString().isNotEmpty() && date == null) {
                Toast.makeText(
                    requireContext(),
                    "Please enter date with correct format (dd/MM/yyyy)",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            if (!userData.kidObject?.id.isNullOrEmpty() && !userData.id.isNullOrEmpty()) {
                scheduleMeet(userData.kidObject?.id!!, userData.id!!, date)
            }
        }

    }


    private fun scheduleMeet(kidId: String, parentId: String, date: Date?) {
        val token = "Bearer ${SharedPref(requireContext()).getToken()}"
        var scheduleMeetRequest = ScheduleMeetRequest(
            SharedPref(requireContext()).getSelectedKid()!!,
            parentId,
            kidId,
            selectedActivity,
            date
        )

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
                        hideProgressBar()
                        Toast.makeText(requireContext(), "Meet Scheduled", Toast.LENGTH_LONG).show()
                        requireActivity().onBackPressed()
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


    private fun setSpinnerAdapter() {
        val others = activitiesMainList
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_text_gender, others
        )
        binding.spinnerActivity.adapter = adapter
        binding.autoCompleteTextView.setAdapter(adapter)

        binding.spinnerActivity.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long,
                ) {
                    selectedActivity = if (position > 0) {
                        others[position]
                    } else {
                        ""
                    }

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMeetDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(data: UsersItem) =
            MeetDetailsFragment().apply {
                userData = data
            }
    }
}