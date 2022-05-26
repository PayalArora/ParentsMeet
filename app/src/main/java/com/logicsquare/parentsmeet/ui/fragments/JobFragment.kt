package com.logicsquare.parentsmeet.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.databinding.FragmentJobBinding
import com.logicsquare.parentsmeet.databinding.JobFilterBinding
import com.logicsquare.parentsmeet.model.*
import com.logicsquare.parentsmeet.network.APIClient
import com.logicsquare.parentsmeet.network.APIInterface
import com.logicsquare.parentsmeet.ui.adapter.JobsAdapter
import com.logicsquare.parentsmeet.utils.*
import com.logicsquare.parentsmeet.views.VButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobFragment : Fragment(), JobsAdapter.OnItemClickListener {

    private lateinit var binding: FragmentJobBinding

    private var category: JOBTYPE = JOBTYPE.ALL
    var jobtype:ArrayList<String> = arrayListOf()
    var experienceRequirement:ArrayList<String> = arrayListOf()
    var educationRequirement:ArrayList<String> = arrayListOf()
    var locationPreference:ArrayList<String> = arrayListOf()
    var jobCategory:ArrayList<String> = arrayListOf()
    var payRange:ArrayList<String> = arrayListOf()

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
        binding.ivFilter.setOnClickListener { bottomSheetWork() }
        checkFilter()
        getAllJobs(null)
    }

    private fun checkFilter() {

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
        payRange.clear()
        jobCategory.clear()
        bottomSheetBinding.showResult.setOnClickListener {
            if (bottomSheetBinding.fullTime.isChecked){
                jobtype.add(EnumUtils.jobType.FullTime.name)
            }else if (bottomSheetBinding.contract.isChecked){
                jobtype.add(EnumUtils.jobType.Contract.name)
            }else if (bottomSheetBinding.partTime.isChecked){
                jobtype.add(EnumUtils.jobType.PartTime.name)
            }

            if (bottomSheetBinding.remote.isChecked){
                locationPreference.add(EnumUtils.locationPreference.Remote.name)
            }else if (bottomSheetBinding.inPerson.isChecked){
                locationPreference.add(EnumUtils.locationPreference.InPerson.name)
            }else if (bottomSheetBinding.hybrid.isChecked){
                locationPreference.add(EnumUtils.locationPreference.Hybrid.name)
            }

            if (bottomSheetBinding.allEducation.isChecked){
                educationRequirement.add(EnumUtils.educationRequirement.AllEducationLevels.name)
            }else if (bottomSheetBinding.master.isChecked){
                educationRequirement.add(EnumUtils.educationRequirement.MasterDegree.name)
            }else if (bottomSheetBinding.bachelor.isChecked){
                educationRequirement.add(EnumUtils.educationRequirement.BachelorDegree.name)
            }else if (bottomSheetBinding.assosiates.isChecked){
                educationRequirement.add(EnumUtils.educationRequirement.AssociateDegree.name)
            }else if (bottomSheetBinding.highSchool.isChecked){
                educationRequirement.add(EnumUtils.educationRequirement.HighSchoolDegree.name)
            }

            if (bottomSheetBinding.allExperience.isChecked){
                experienceRequirement.add(EnumUtils.experienceRequirement.AllExperience.name)
            }else if (bottomSheetBinding.entryLevel.isChecked){
                experienceRequirement.add(EnumUtils.experienceRequirement.EntryLevel.name)
            }else if (bottomSheetBinding.midLevel.isChecked){
                experienceRequirement.add(EnumUtils.experienceRequirement.MidLevel.name)
            }else if (bottomSheetBinding.seniorLevel.isChecked){
                experienceRequirement.add(EnumUtils.experienceRequirement.SeniorLevel.name)
            }

            if (bottomSheetBinding.market.isChecked){
                jobCategory.add(getString(R.string.market))
            }else if (bottomSheetBinding.sales.isChecked){
                jobCategory.add(getString(R.string.sales))
            }else if (bottomSheetBinding.tech.isChecked){
                jobCategory.add(getString(R.string.tech))
            }else if (bottomSheetBinding.administration.isChecked){
                jobCategory.add(getString(R.string.administration))
            }else if (bottomSheetBinding.allJob.isChecked){
                jobCategory.add(getString(R.string.all_job))
            }

            if (bottomSheetBinding.fifty.isChecked){
                payRange.add(getString(R.string.fifty))
            }else if (bottomSheetBinding.sixty.isChecked){
                payRange.add(getString(R.string.sixty))
            }else if (bottomSheetBinding.seventyfive.isChecked){
                payRange.add(getString(R.string.seventyfive))
            }else if (bottomSheetBinding.eightyfive.isChecked){
                payRange.add(getString(R.string.eightyfive))
            }else if (bottomSheetBinding.lakh.isChecked){
                payRange.add(getString(R.string.lakh))
            }
            getAllJobs(dialog)
           // dialog.dismiss()

        }


        bottomSheetBinding.close.setOnClickListener{
            dialog.dismiss()
        }
    }

    private fun getAllJobs(dialog: BottomSheetDialog?) {
        val token = "Bearer ${SharedPref(requireContext()).getToken()}"
        var addJobRequest= AddJobRequest()
        addJobRequest.filters.category = category.jobtype
        addJobRequest.filters.payRange = payRange
        addJobRequest.filters.experienceRequirement = experienceRequirement
        addJobRequest.filters.educationRequirement = educationRequirement
        addJobRequest.filters.jobtype =jobtype
        addJobRequest.filters.locationPreference =locationPreference
        addJobRequest.filters.jobCategory =jobCategory
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

}
