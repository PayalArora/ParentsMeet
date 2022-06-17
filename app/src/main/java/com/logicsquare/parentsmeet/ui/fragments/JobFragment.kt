package com.logicsquare.parentsmeet.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.databinding.FragmentJobBinding
import com.logicsquare.parentsmeet.databinding.JobFilterBinding
import com.logicsquare.parentsmeet.databinding.LayoutMeetFilterBinding
import com.logicsquare.parentsmeet.model.*
import com.logicsquare.parentsmeet.network.APIClient
import com.logicsquare.parentsmeet.network.APIInterface
import com.logicsquare.parentsmeet.ui.adapter.JobFilterAdapter
import com.logicsquare.parentsmeet.ui.adapter.JobsAdapter
import com.logicsquare.parentsmeet.ui.adapter.OnItemClickEvent
import com.logicsquare.parentsmeet.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobFragment : Fragment(), OnItemClickEvent, JobsAdapter.OnItemClickListener {

    private lateinit var binding: FragmentJobBinding

    private var category: JOBTYPE = JOBTYPE.ALL
    var jobtype: ArrayList<String> = arrayListOf()
    var experienceRequirement: ArrayList<String> = arrayListOf()
    var educationRequirement: ArrayList<String> = arrayListOf()
    var locationPreference: ArrayList<String> = arrayListOf()
    var jobCategory: ArrayList<String> = arrayListOf()
    var mPayrange: Int = 50000

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvApplied.setOnClickListener {
            binding.tvApplied.setBackgroundResource(R.drawable.background_normal_button)
            binding.tvSaved.setBackgroundResource(R.drawable.background_gradient)
            binding.tvAll.setBackgroundResource(R.drawable.background_gradient)

            binding.tvApplied.setTextColor(resources.getColor(R.color.dark_green))
            binding.tvSaved.setTextColor(resources.getColor(R.color.white))
            binding.tvAll.setTextColor(resources.getColor(R.color.white))
            category = JOBTYPE.JOBAPPLIED

            getAllJobs(null)
        }

        binding.tvSaved.setOnClickListener {
            binding.tvSaved.setBackgroundResource(R.drawable.background_normal_button)
            binding.tvApplied.setBackgroundResource(R.drawable.background_gradient)
            binding.tvAll.setBackgroundResource(R.drawable.background_gradient)

            binding.tvApplied.setTextColor(resources.getColor(R.color.white))
            binding.tvSaved.setTextColor(resources.getColor(R.color.dark_green))
            binding.tvAll.setTextColor(resources.getColor(R.color.white))
            category = JOBTYPE.JOBSAVED

            getAllJobs(null)
        }

        binding.tvAll.setOnClickListener {
            binding.tvSaved.setBackgroundResource(R.drawable.background_gradient)
            binding.tvApplied.setBackgroundResource(R.drawable.background_gradient)
            binding.tvAll.setBackgroundResource(R.drawable.background_normal_button)

            binding.tvApplied.setTextColor(resources.getColor(R.color.white))
            binding.tvSaved.setTextColor(resources.getColor(R.color.white))
            binding.tvAll.setTextColor(resources.getColor(R.color.dark_green))
            category = JOBTYPE.ALL

            getAllJobs(null)
        }
        binding.ivFilter.setOnClickListener {
            bottomSheetWork()
           // checkFilter()

        }
        //checkFilter()
        getAllJobs(null)
    }



    fun bottomSheetWork() {
        var bottomSheetBinding: JobFilterBinding = JobFilterBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(bottomSheetBinding.root)
        dialog.show()
        jobtype.clear()
        locationPreference.clear()
        educationRequirement.clear()
        experienceRequirement.clear()
        jobCategory.clear()
        getSettings(bottomSheetBinding)
        bottomSheetBinding.showResult.setOnClickListener {
//            if (bottomSheetBinding.fullTime.isChecked){
//                jobtype.add(EnumUtils.jobType.FullTime.type)
//            }else if (bottomSheetBinding.contract.isChecked){
//                jobtype.add(EnumUtils.jobType.Contract.type)
//            }else if (bottomSheetBinding.partTime.isChecked){
//                jobtype.add(EnumUtils.jobType.PartTime.type)
//            }
            if (bottomSheetBinding.remote.isChecked) {
                locationPreference.add(EnumUtils.locationPreference.Remote.name)
            }
            if (bottomSheetBinding.inPerson.isChecked) {
                locationPreference.add(EnumUtils.locationPreference.InPerson.name)
            }
            if (bottomSheetBinding.hybrid.isChecked) {
                locationPreference.add(EnumUtils.locationPreference.Hybrid.name)
            }

            if (bottomSheetBinding.allEducation.isChecked) {
                educationRequirement.add(EnumUtils.educationRequirement.AllEducationLevels.name)
            }
            if (bottomSheetBinding.master.isChecked) {
                educationRequirement.add(EnumUtils.educationRequirement.MasterDegree.name)
            }
            if (bottomSheetBinding.bachelor.isChecked) {
                educationRequirement.add(EnumUtils.educationRequirement.BachelorDegree.name)
            }
            if (bottomSheetBinding.assosiates.isChecked) {
                educationRequirement.add(EnumUtils.educationRequirement.AssociateDegree.name)
            }
            if (bottomSheetBinding.highSchool.isChecked) {
                educationRequirement.add(EnumUtils.educationRequirement.HighSchoolDegree.name)
            }

            if (bottomSheetBinding.allExperience.isChecked) {
                experienceRequirement.add(EnumUtils.experienceRequirement.AllExperience.name)
            }
            if (bottomSheetBinding.entryLevel.isChecked) {
                experienceRequirement.add(EnumUtils.experienceRequirement.EntryLevel.name)
            }
            if (bottomSheetBinding.midLevel.isChecked) {
                experienceRequirement.add(EnumUtils.experienceRequirement.MidLevel.name)
            }
            if (bottomSheetBinding.seniorLevel.isChecked) {
                experienceRequirement.add(EnumUtils.experienceRequirement.SeniorLevel.name)
            }

//            if (bottomSheetBinding.market.isChecked) {
//                jobCategory.add(getString(R.string.market))
//            } else if (bottomSheetBinding.sales.isChecked) {
//                jobCategory.add(getString(R.string.sales))
//            } else if (bottomSheetBinding.tech.isChecked) {
//                jobCategory.add(getString(R.string.tech))
//            } else if (bottomSheetBinding.administration.isChecked) {
//                jobCategory.add(getString(R.string.administration))
//            } else if (bottomSheetBinding.allJob.isChecked) {
//                jobCategory.add(getString(R.string.all_job))
//            }

            if (bottomSheetBinding.fifty.isChecked) {
                mPayrange = 50000
            } else if (bottomSheetBinding.sixty.isChecked) {
                mPayrange = 60000
            } else if (bottomSheetBinding.seventyfive.isChecked) {
                mPayrange = 75000
            } else if (bottomSheetBinding.eightyfive.isChecked) {
                mPayrange = 85000
            } else if (bottomSheetBinding.lakh.isChecked) {
                mPayrange = 100000
            }

            getAllJobs(dialog)
            // dialog.dismiss()

        }
        bottomSheetBinding.salaryGroup.setOnCheckedChangeListener { radioGroup, i ->
            val idRadioButtonChosen: Int = radioGroup.getCheckedRadioButtonId()
            if (idRadioButtonChosen > 0) {
                bottomSheetBinding.salaryGroup2.clearCheck()
                radioGroup.check(idRadioButtonChosen)
            }

        }
        bottomSheetBinding.salaryGroup2.setOnCheckedChangeListener { radioGroup, i ->
            val idRadioButtonChosen: Int = radioGroup.getCheckedRadioButtonId()

            if (idRadioButtonChosen > 0) {
                bottomSheetBinding.salaryGroup.clearCheck()
                radioGroup.check(idRadioButtonChosen)
            }
        }

        bottomSheetBinding.close.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun getSettings(bottomSheetBinding: JobFilterBinding) {
        val token = "Bearer ${SharedPref(requireContext()).getToken()}"
        val call: Call<SettingResponse?> =
            APIClient.client.create(APIInterface::class.java).getSettings(token)
        showProgressBar()
        call.enqueue(object : Callback<SettingResponse?> {
            override fun onResponse(
                call: Call<SettingResponse?>,
                response: Response<SettingResponse?>,
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        handleSettingsResponse(response.body()!!, bottomSheetBinding)
                    }
                } else {
                    handleErrorResponse(response.errorBody(), requireContext())
                }
                hideProgressBar()
            }

            override fun onFailure(call: Call<SettingResponse?>, t: Throwable) {
                hideProgressBar()
                showToast(t.localizedMessage)
            }
        })
    }

    private fun handleSettingsResponse(
        body: SettingResponse,
        bottomSheetBinding: JobFilterBinding,
    ) {
        bottomSheetBinding.rvJobType.apply {
            val gridLayoutManager = GridLayoutManager(requireContext(), 3)
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL)
            layoutManager = gridLayoutManager
            body?.setting?.preferences?.jobTypes.let {
                adapter = JobFilterAdapter(0, it as List<Any?> , this@JobFragment)
            }
        }

        bottomSheetBinding.rvJobCategory.apply {
            val gridLayoutManager = GridLayoutManager(requireContext(), 3)
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL)
            layoutManager = gridLayoutManager
            body?.setting?.preferences?.jobCategories.let {
                adapter = JobFilterAdapter(1, it as List<Any?>, this@JobFragment)
            }
        }
    }

    private fun getAllJobs(dialog: BottomSheetDialog?) {
        val token = "Bearer ${SharedPref(requireContext()).getToken()}"
        var addJobRequest = AddJobRequest()
        addJobRequest.filters.category = category.jobtype
        addJobRequest.filters.payRange.min = mPayrange
        addJobRequest.filters.experienceRequirement = experienceRequirement
        addJobRequest.filters.educationRequirement = educationRequirement
        addJobRequest.filters.jobtype = jobtype
        addJobRequest.filters.locationPreference = locationPreference
        addJobRequest.filters.jobCategory = jobCategory
        addJobRequest.limit = 100
        val call: Call<JobsResponse?> =
            APIClient.client.create(APIInterface::class.java).getJobs(token, addJobRequest)
        showProgressBar()
        call.enqueue(object : Callback<JobsResponse?> {
            override fun onResponse(
                call: Call<JobsResponse?>,
                response: Response<JobsResponse?>,
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        dialog?.dismiss()
                        handleResponse(response.body()!!)
                    }
                } else {
                    handleErrorResponse(response.errorBody(), requireContext())
                }
                hideProgressBar()
            }

            override fun onFailure(call: Call<JobsResponse?>, t: Throwable) {
                hideProgressBar()
                showToast(t.localizedMessage)
            }
        })
    }

    private fun handleResponse(response: JobsResponse) {
        var adapter =
            JobsAdapter((response.jobs as ArrayList<JobsItem>), requireContext())
        adapter.setListener(this)
        binding.rvJobs.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentJobBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack("JobDetailsFragment")
        transaction.commit()
    }

    override fun onClick(job: JobsItem) {
        loadFragment(JobDetailsFragment(job.jobId!!))
    }

    override fun applyJob(job: JobsItem): JobsItem {
        val token = "Bearer ${SharedPref(requireContext()).getToken()}"
        val call: Call<JobAppliedSavedResponse?> =
            APIClient.client.create(APIInterface::class.java).applyJob(token, job?.jobId!!)
        showProgressBar()
        call.enqueue(object : Callback<JobAppliedSavedResponse?> {
            override fun onResponse(
                call: Call<JobAppliedSavedResponse?>,
                response: Response<JobAppliedSavedResponse?>,
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        job.isJobApplied = 1
                        binding.rvJobs.adapter?.notifyDataSetChanged()
                        showToast("Job Applied")
                    }
                } else {
                    handleErrorResponse(response.errorBody(), requireContext())
                }
                hideProgressBar()
            }

            override fun onFailure(call: Call<JobAppliedSavedResponse?>, t: Throwable) {
                hideProgressBar()
                showToast(t.localizedMessage)
            }
        })
        return job
    }


    override fun saveJob(job: JobsItem): JobsItem {
        val token = "Bearer ${SharedPref(requireContext()).getToken()}"
        val call: Call<JobAppliedSavedResponse?> =
            APIClient.client.create(APIInterface::class.java).saveJob(token, job?.jobId!!)
        showProgressBar()
        call.enqueue(object : Callback<JobAppliedSavedResponse?> {
            override fun onResponse(
                call: Call<JobAppliedSavedResponse?>,
                response: Response<JobAppliedSavedResponse?>,
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        job.isJobSaved = 1
                        binding.rvJobs.adapter?.notifyDataSetChanged()
                        showToast("Job Saved")
                    }
                } else {
                    handleErrorResponse(response.errorBody(), requireContext())
                }
                hideProgressBar()
            }

            override fun onFailure(call: Call<JobAppliedSavedResponse?>, t: Throwable) {
                hideProgressBar()
                showToast(t.localizedMessage)
            }
        })
        return job
    }

    override fun onItemClick(position: ArrayList<String>, type: Int) {
        if (type == 0) {
            jobtype.clear()
            jobtype.addAll(position)
        } else {
            jobCategory.clear()
            jobCategory.addAll(position)

        }
    }

}
