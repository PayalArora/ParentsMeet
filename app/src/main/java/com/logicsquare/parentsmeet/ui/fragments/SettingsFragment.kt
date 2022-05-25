package com.logicsquare.parentsmeet.ui.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.ui.adapter.ChildPagerAdapter
import com.logicsquare.parentsmeet.databinding.FragmentSettingsBinding
import com.logicsquare.parentsmeet.model.Kid
import com.logicsquare.parentsmeet.model.KidsItem
import com.logicsquare.parentsmeet.model.ProfileRequest
import com.logicsquare.parentsmeet.model.ProfileResponse
import com.logicsquare.parentsmeet.network.APIClient
import com.logicsquare.parentsmeet.network.APIInterface
import com.logicsquare.parentsmeet.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SettingsFragment : Fragment(), DatePickerDialog.OnDateSetListener, KidsData {
    private lateinit var mBinding: FragmentSettingsBinding
    private var myCalendar: Calendar = Calendar.getInstance()
    private lateinit var childPagerAdapter: ChildPagerAdapter
    var list: List<KidsItem?>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSettingsBinding.inflate(inflater, container, false)

        getProfile()
        setListeners()
        childPagerAdapter = ChildPagerAdapter(this, list, this)
        mBinding.pager.adapter = childPagerAdapter
        TabLayoutMediator(mBinding.tabLayout, mBinding.pager) { tab, position ->
        }.attach()
        return mBinding.root
    }

    private fun addParentsData() {
        var  interests: ArrayList<String> = ArrayList()
        if (mBinding.brunchDate.isChecked){
            interests.add(brunch)
        }
        if (mBinding.lunchDate.isChecked){
            interests.add(lunchDate)
        }
        if (mBinding.dinnerDate.isChecked){
            interests.add(dinnerDate)
        }
        if (mBinding.parkTrip.isChecked){
            interests.add(parkTrip)
        }
        var profileRequest = ProfileRequest()
        profileRequest.email = mBinding.etEmailId.text.toString().trim()
        profileRequest.name.first = mBinding.etFirstName.getText().toString().trim()
        profileRequest.name.last = mBinding.etLastName.getText().toString().trim()
        profileRequest.profession = mBinding.etProfession.getText().toString().trim()
        profileRequest.phoneCountryCode = "+91"
        profileRequest.phone = mBinding.etCellphone.text.toString().trim().replace("+91","")
        profileRequest.dob.day = mBinding.etDob.text.toString().split("/")[0]
        profileRequest.dob.month = mBinding.etDob.text.toString().split("/")[1]
        profileRequest.dob.year = mBinding.etDob.text.toString().split("/")[2]
        profileRequest.preferences.parentInterests = interests

        val token = "Bearer ${SharedPref(requireContext()).getToken()}"
        val call: Call<ProfileResponse?> =
            APIClient.client.create(APIInterface::class.java).updateProfile(token, profileRequest)
        showProgressBar()
        call.enqueue(object : Callback<ProfileResponse?> {
            override fun onResponse(
                call: Call<ProfileResponse?>,
                response: Response<ProfileResponse?>,
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null){
                        showToast(getString(R.string.saved))
                    }
                      handleResponse(response.body()!!)
                } else{
                    handleErrorResponse(response.errorBody(), requireContext())
                }
                hideProgressBar()
            }

            override fun onFailure(call: Call<ProfileResponse?>, t: Throwable) {
                hideProgressBar()
                showToast(t.localizedMessage)
            }
        })
    }

    private fun setListeners() {
        mBinding.etDob.setOnClickListener {
            val dialog = DatePickerDialog(
                requireContext(), this,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            )
            dialog.datePicker.maxDate = System.currentTimeMillis() - 568025136000L;
            dialog.show()
        }
        mBinding.btnSave.setOnClickListener {
          addParentsData()
        }
    }
    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, month)
        myCalendar.set(Calendar.DAY_OF_MONTH, day)
        setDob()
    }
    private fun setDob() {
        val myFormat = "dd/MM/yyyy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        mBinding.etDob.setText(dateFormat.format(myCalendar.time))
    }
    private fun getProfile() {
        val token = "Bearer ${SharedPref(requireContext()).getToken()}"
        val call: Call<ProfileResponse?> =
            APIClient.client.create(APIInterface::class.java).getProfile(token)
        showProgressBar()
        call.enqueue(object : Callback<ProfileResponse?> {
            override fun onResponse(
                call: Call<ProfileResponse?>,
                response: Response<ProfileResponse?>,
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null)
                        handleResponse(response.body()!!)
                } else{
                    handleErrorResponse(response.errorBody(), requireContext())
                }
               hideProgressBar()
            }

            override fun onFailure(call: Call<ProfileResponse?>, t: Throwable) {
                hideProgressBar()
                showToast(t.localizedMessage)
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun handleResponse(profileResponse: ProfileResponse) {
        mBinding.etFirstName.setText( "${profileResponse.user?.name?.first?.toUpperCas()}")
        mBinding.etLastName.setText( "${profileResponse.user?.name?.last?.toUpperCas()}")

       var day: String? = "00"
        if (profileResponse.user?.dob?.day?:0 < 10){
        day = "0${profileResponse.user?.dob?.day}"
        }
        var month: String? = "00"
        if (profileResponse.user?.dob?.month?:0 < 10){
            month = "0${profileResponse.user?.dob?.month}"
        }

        var date:String? = "$day/$month/${profileResponse.user?.dob?.year}"

        mBinding.etDob.setText( date)
        mBinding.etEmailId.setText( "${profileResponse.user?.email}")
        mBinding.etProfession.setText( "${profileResponse.user?.profession?.toUpperCas()}")
        mBinding.etCellphone.setText("${profileResponse.user?.phoneCountryCode}${profileResponse.user?.phone}")
        profileResponse.user?.preferences?.parentInterests?.map { when(it){
            brunch-> mBinding.brunchDate.isChecked = true
            lunchDate-> mBinding.lunchDate.isChecked = true
            parkTrip-> mBinding.parkTrip.isChecked = true
            dinnerDate-> mBinding.dinnerDate.isChecked = true

        } }
        setKidsdata(profileResponse)
    }

    private fun setKidsdata(profileResponse: ProfileResponse) {
        if (profileResponse.user?.kidsCount == 0)
            mBinding.kidsLayout.visibility = GONE
        else {
            mBinding.kidsLayout.visibility = VISIBLE
            if (profileResponse.user?.kids!=null) {
                childPagerAdapter = ChildPagerAdapter(this, profileResponse.user.kids, this)
                mBinding.pager.adapter = childPagerAdapter
            }
            profileResponse.user?.kids?.map {
                mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().setText(it?.name?.capitalizeWords()))
            }
//            TabLayoutMediator(mBinding.tabLayout, mBinding.pager) { tab, position ->
//                profileResponse.user?.kids?.map {tab.text =it?.name}
//            }.attach()
        }
    }

    companion object ParentInterent{
        private  const val brunch = "BRUNCH"
        private  const val dinnerDate = "DINNER_DATE"
        private  const val lunchDate = "LUNCH_DATE"
        private  const val parkTrip = "PARK_TRIP"
    }

    override fun kidsData(kid: Kid?) {
        mBinding.tabLayout.getTabAt( mBinding.tabLayout.selectedTabPosition)?.setText(kid?.name?.capitalizeWords())
    }
}