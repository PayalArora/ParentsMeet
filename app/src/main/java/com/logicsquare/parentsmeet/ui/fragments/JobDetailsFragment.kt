package com.logicsquare.parentsmeet.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.logicsquare.parentsmeet.databinding.FragmentJobDetailsBinding
import com.logicsquare.parentsmeet.model.JobAppliedSavedResponse
import com.logicsquare.parentsmeet.model.JobsDetailsResponse
import com.logicsquare.parentsmeet.network.APIClient
import com.logicsquare.parentsmeet.network.APIInterface
import com.logicsquare.parentsmeet.ui.DrawerActivity
import com.logicsquare.parentsmeet.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobDetailsFragment(id: String) : Fragment() {

    var jobId = id
    lateinit var binding: FragmentJobDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobDetailsBinding.inflate(inflater)

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.menuOption.setOnClickListener { startActivity(Intent(requireActivity(), DrawerActivity::class.java)) }

        getJobDetails()
        return binding.root
    }


    private fun getJobDetails() {
        val token = "Bearer ${SharedPref(requireContext()).getToken()}"
        val call: Call<JobsDetailsResponse?> =
            APIClient.client.create(APIInterface::class.java).getJobDetails(token, jobId)
        showProgressBar()
        call.enqueue(object : Callback<JobsDetailsResponse?> {
            override fun onResponse(
                call: Call<JobsDetailsResponse?>,
                response: Response<JobsDetailsResponse?>,
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

            override fun onFailure(call: Call<JobsDetailsResponse?>, t: Throwable) {
                hideProgressBar()
                showToast(t.localizedMessage)
            }
        })

    }

    private fun handleResponse(jobsResponse: JobsDetailsResponse) {
        var locationPreference = ""
        jobsResponse.job?.locationPreference?.let {
            if (it.equals(EnumUtils.locationPreference.Hybrid.name)) {

                locationPreference = EnumUtils.locationPreference.Hybrid.location
            } else if (it.equals(EnumUtils.locationPreference.InPerson.name)) {
                locationPreference = EnumUtils.locationPreference.InPerson.location
            } else {
                locationPreference = EnumUtils.locationPreference.Remote.location
            }
        }
        var experienceRequirement = ""
        jobsResponse.job?.experienceRequirement?.let {
            if (it.equals(EnumUtils.experienceRequirement.AllExperience.name)) {

                experienceRequirement = EnumUtils.experienceRequirement.AllExperience.degree
            } else if (it.equals(EnumUtils.experienceRequirement.EntryLevel.name)) {

                experienceRequirement = EnumUtils.experienceRequirement.EntryLevel.degree
            } else if (it.equals(EnumUtils.experienceRequirement.MidLevel.name)) {

                experienceRequirement = EnumUtils.experienceRequirement.MidLevel.degree
            }else if (it.equals(EnumUtils.experienceRequirement.SeniorLevel.name)) {

                experienceRequirement = EnumUtils.experienceRequirement.SeniorLevel.degree
            }
        }

        var educationRequirement = ""
        jobsResponse.job?.educationRequirement?.let {
            if (it.equals(EnumUtils.educationRequirement.AssociateDegree.name)) {

                educationRequirement = EnumUtils.educationRequirement.AssociateDegree.eduction
            } else  if (it.equals(EnumUtils.educationRequirement.AllEducationLevels.name)) {

                educationRequirement = EnumUtils.educationRequirement.AllEducationLevels.eduction
            }else  if (it.equals(EnumUtils.educationRequirement.BachelorDegree.name)) {

                educationRequirement = EnumUtils.educationRequirement.BachelorDegree.eduction
            }else  if (it.equals(EnumUtils.educationRequirement.HighSchoolDegree.name)) {

                educationRequirement = EnumUtils.educationRequirement.HighSchoolDegree.eduction
            }else  if (it.equals(EnumUtils.educationRequirement.MasterDegree.name)) {

                educationRequirement = EnumUtils.educationRequirement.MasterDegree.eduction
            }
        }

        var jobtype = ""
        jobsResponse.job?.jobType?.let {
            if (it.equals(EnumUtils.jobType.Contract.name)) {

                jobtype = EnumUtils.jobType.Contract.type
            } else if (it.equals(EnumUtils.jobType.FullTime.name)) {

                jobtype = EnumUtils.jobType.FullTime.type
            }else if (it.equals(EnumUtils.jobType.Temporary.name)) {

                jobtype = EnumUtils.jobType.Temporary.type
            }

        }




        binding.llContainer.addView(
            addJobLocation(
                "Job location : ",
                "${jobsResponse.job?.address?.zip} / ${locationPreference}",
                0
            )
        )
        binding.llContainer.addView(
            addJobLocation(
                "About company : ",
                "${jobsResponse.job?.aboutCompany} (${jobsResponse.job?.jobType})/${jobsResponse.job?.locationPreference}",
                0
            )
        )
        binding.llContainer.addView(
            addJobLocation(
                "Position : ",
                "${jobsResponse.job?.title} (${jobsResponse.job?.experienceRequirement})/${jobsResponse.job?.educationRequirement}",
                0
            )
        )
        binding.llContainer.addView(addJobLocation("Number of opening : ", "1", 0))
        binding.llContainer.addView(
            addJobLocation(
                "Experience : ",
                "${jobsResponse.job?.experienceRequirement}",
                0
            )
        )
        binding.llContainer.addView(
            addJobLocation(
                "Job Description : ",
                "${jobsResponse.job?.description}",
                1
            )
        )
        binding.llContainer.addView(
            addJobLocation(
                "Roles and Responsibilities : ",
                "${jobsResponse.job?.rolesAndResponsibilities}",
                1
            )
        )
        binding.llContainer.addView(
            addJobLocation(
                "Desired Candidate Profile : ",
                "${jobsResponse.job?.desiredCandidateProfile}",
                1
            )
        )

        binding.tvTitle.text = jobsResponse.job?.title
        binding.tvLocation.text = jobsResponse.job?.address?.zip
        binding.tvDesc.text = Html.fromHtml(jobsResponse.job?.description)
        binding.tvType.text = "${
            jobsResponse.job?.createdAt?.let {
                requireContext().timeConverter(it)
            }
        } | ${jobtype}"

        if (jobsResponse.job?.isJobApplied == 0) {
            binding.tvApplied.text = "Apply"
            binding.tvApplyNow.visibility = View.VISIBLE
            binding.tvApplied.visibility = View.GONE
        } else {
            binding.tvApplied.text = "Applied"
            binding.tvApplyNow.visibility = View.GONE
            binding.tvApplied.visibility = View.VISIBLE
        }
        if (jobsResponse.job?.isJobSaved == 0) {
            binding.tvSaved.visibility = View.GONE
        } else {
            binding.tvSaved.visibility = View.VISIBLE
            binding.tvSaved.text = "Saved"
        }

        binding.tvApplyNow.setOnClickListener {
            if (jobsResponse.job?.isJobApplied == 0) {
                applyJob()
            }
        }

        val circularProgressDrawable = context?.getProgressDrawable()
        circularProgressDrawable?.start()
        if (!jobsResponse.job?.logo.isNullOrEmpty()) {
            Glide.with(this).load(jobsResponse.job?.logo).centerCrop()
                .placeholder(circularProgressDrawable).into(binding.view)
        }
    }

    private fun addJobLocation(tittle: String?, desc: String?, orientation: Int): View {
        return JobLayoutDetail(
            requireContext(),
            tittle,
            desc,
            orientation
        )
    }

    private fun applyJob() {
        val token = "Bearer ${SharedPref(requireContext()).getToken()}"
        val call: Call<JobAppliedSavedResponse?> =
            APIClient.client.create(APIInterface::class.java).applyJob(token, jobId)
        showProgressBar()
        call.enqueue(object : Callback<JobAppliedSavedResponse?> {
            override fun onResponse(
                call: Call<JobAppliedSavedResponse?>,
                response: Response<JobAppliedSavedResponse?>,
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        binding.tvApplied.text = "Applied"
                        binding.tvApplyNow.visibility = View.GONE
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
    }

    private fun saveJob() {
        val token = "Bearer ${SharedPref(requireContext()).getToken()}"
        val call: Call<JobAppliedSavedResponse?> =
            APIClient.client.create(APIInterface::class.java).saveJob(token, jobId)
        showProgressBar()
        call.enqueue(object : Callback<JobAppliedSavedResponse?> {
            override fun onResponse(
                call: Call<JobAppliedSavedResponse?>,
                response: Response<JobAppliedSavedResponse?>,
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        binding.tvSaved.text = "Saved"
//                        showToast("Job Saved")
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
    }
}



