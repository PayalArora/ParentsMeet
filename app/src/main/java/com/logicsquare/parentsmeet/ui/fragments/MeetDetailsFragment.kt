package com.logicsquare.parentsmeet.ui.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.view.*
import android.widget.*
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.toLowerCase
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
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MeetDetailsFragment : Fragment() {

    lateinit var binding: FragmentMeetDetailsBinding
    lateinit var userData: UsersItem
    var activitiesMainList = arrayListOf<String>()
    var selectedActivity = ""
    var adapter: ArrayAdapter<String>? = null
    var cal = Calendar.getInstance()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvParentName.text =
            "${userData.name?.first} ${userData.name?.last}".capitalizeWords()
        binding.tvName.text =
            "Meet ${userData.name?.first} ${userData.name?.last}".capitalizeWords()

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

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
                parentInterests = if (parentInterests.isNotEmpty())
                    "$parentInterests, $item, "
                else
                    item
            }

        parentInterests = parentInterests.removeSuffix(", ")
        parentInterests = parentInterests.toLowerCase().capitalizeWords()

        var childInterests: String = ""

        if (!userData.kidObject?.preferences?.activities.isNullOrEmpty())
            for (item in userData.kidObject?.preferences?.activities!!) {
                childInterests = if (childInterests.isNotEmpty())
                    "$childInterests, $item, "
                else
                    item
            }

        if (!userData.kidObject?.preferences?.games.isNullOrEmpty())
            for (item in userData.kidObject?.preferences?.games!!) {
                childInterests = if (childInterests.isNotEmpty())
                    "$childInterests, $item, "
                else
                    item
            }

        childInterests = childInterests.removeSuffix(", ")
        childInterests = childInterests.capitalizeWords()

        binding.tvParentLocation.text = userData.phoneCountryCode
        binding.tvParentInterests.text = parentInterests

        binding.tvChildName.text = userData.kidObject?.name?.capitalizeWords()
        binding.tvChildAge.text = userData.kidObject?.age.toString()

        var timings: String = ""

        if (!userData.kidObject?.preferences?.timings.isNullOrEmpty())
            for (item in userData.kidObject?.preferences?.timings!!) {
                timings = "$timings, $item ,"
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
            /*val time: String = binding.edtTime.text.toString().ifEmpty { "00:00" }
            Log.e("time= ", "$time")
            var date: Date? = validateDate("${binding.edtCalender.text.toString()} $time}")
            if (binding.edtCalender.text.toString().isNotEmpty() && date == null) {
                Toast.makeText(
                    requireContext(),
                    "Please enter date with correct format (dd/MM/yyyy)",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }*/
            selectedActivity = binding.autoCompleteTextView.text.toString()
            if (!userData.kidObject?.id.isNullOrEmpty() && !userData.id.isNullOrEmpty()) {
                if (selectedActivity.isEmpty()) {
                    Toast.makeText(requireContext(), "Please Select a activity", Toast.LENGTH_LONG)
                        .show()
                    return@setOnClickListener
                } else if (binding.edtCalender.text.isEmpty()) {
                    Toast.makeText(requireContext(), "Please Select a Date", Toast.LENGTH_LONG)
                        .show()
                    return@setOnClickListener
                } else if (binding.edtTime.text.isEmpty()) {
                    Toast.makeText(requireContext(), "Please Select a Time", Toast.LENGTH_LONG)
                        .show()
                    return@setOnClickListener
                }

                var date: Date? = validateDate("${binding.edtCalender.text} ${binding.edtTime.text}}")
                scheduleMeet(userData.kidObject?.id!!, userData.id!!, date)
            }
        }

        initDatePicker()

    }

    private fun initDatePicker() {
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }


        binding.edtCalender.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        val timeSetListener =
            TimePickerDialog.OnTimeSetListener { timePicker: TimePicker, hourOfDay: Int, minute: Int ->
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);
                binding.edtTime.text = "${hourOfDay}:${minute}"
            }

        binding.edtTime.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            var hour = calendar.get(Calendar.HOUR)
            var minute = calendar.get(Calendar.MINUTE)
            TimePickerDialog(requireContext(), timeSetListener, hour, minute, true).show()

        }
    }


    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.edtCalender.text = sdf.format(cal.time)
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
        adapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_text_gender, activitiesMainList
        )
        binding.spinnerActivity.adapter = adapter
        binding.autoCompleteTextView.setAdapter(adapter)

        binding.spinnerActivity.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long,
                ) {
                    /* if (activitiesMainList[position] == "Other"){
                         showDialog(requireContext())
                     }*/
                    selectedActivity = if (position > 0) {
                        activitiesMainList[position]
                    } else {
                        ""
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

    }


    private fun showDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_tags_dialog)
        dialog.window?.setBackgroundDrawableResource(R.drawable.bg_white_rect)
        val width: Int =
            (context.getResources().getDisplayMetrics().widthPixels) - 140 //<-- int width=400;
        dialog.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        val skipBtn = dialog.findViewById(R.id.btn_skip) as Button
        val title = dialog.findViewById(R.id.title) as TextView
        val submitBtn = dialog.findViewById(R.id.btn_submit) as Button
        val codeTxt = dialog.findViewById<EditText>(R.id.code_txt)

        title.text = "Add a Activity"

        skipBtn.setOnClickListener {
            dialog.dismiss()
        }
        submitBtn.setOnClickListener {
            if (codeTxt.text.isNotEmpty()) {
                activitiesMainList.add(codeTxt.text.toString())
                adapter?.notifyDataSetChanged()
                binding.spinnerActivity.setSelection(activitiesMainList.size - 1)
            } else {
                showToast(getString(R.string.add_tag_error))
            }
            dialog.dismiss()
        }

        dialog.show()
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