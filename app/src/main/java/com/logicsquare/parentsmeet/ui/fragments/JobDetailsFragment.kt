package com.logicsquare.parentsmeet.ui.fragments

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.logicsquare.parentsmeet.databinding.FragmentJobDetailsBinding
import com.logicsquare.parentsmeet.model.JobAppliedSavedResponse
import com.logicsquare.parentsmeet.model.JobsdetailResponse
import com.logicsquare.parentsmeet.network.APIClient
import com.logicsquare.parentsmeet.network.APIInterface
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

        getJobDetails()
        return binding.root
    }


    private fun getJobDetails() {
        val token = "Bearer ${SharedPref(requireContext()).getToken()}"
        val call: Call<JobsdetailResponse?> =
            APIClient.client.create(APIInterface::class.java).getJobDetails(token, jobId)
        showProgressBar()
        call.enqueue(object : Callback<JobsdetailResponse?> {
            override fun onResponse(
                call: Call<JobsdetailResponse?>,
                response: Response<JobsdetailResponse?>,
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

            override fun onFailure(call: Call<JobsdetailResponse?>, t: Throwable) {
                hideProgressBar()
                showToast(t.localizedMessage)
            }
        })

    }

    private fun handleResponse(jobsResponse: JobsdetailResponse) {
        binding.llContainer.addView(
            addJobLocation(
                "Job location : ",
                "${jobsResponse.job?.address?.zip} (${jobsResponse.job?.jobType})/${jobsResponse.job?.locationPreference}",
                0
            )
        )
        binding.llContainer.addView(
            addJobLocation(
                "About company : ",
                "${jobsResponse.job?.address?.zip} (${jobsResponse.job?.jobType})/${jobsResponse.job?.locationPreference}",
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
                "${jobsResponse.job?.description}",
                1
            )
        )
        binding.llContainer.addView(
            addJobLocation(
                "Desired Candidate Profile : ",
                "${jobsResponse.job?.description}",
                1
            )
        )

        binding.tvTitle.text = jobsResponse.job?.title
        binding.tvLocation.text = jobsResponse.job?.address?.zip
        binding.tvDesc.text = Html.fromHtml(jobsResponse.job?.description)
        binding.tvType.text = jobsResponse.job?.jobType

        if (jobsResponse.job?.isJobApplied == 0) {
            binding.tvApplied.text = "Apply"
            binding.tvApplyNow.visibility = View.VISIBLE
        } else {
            binding.tvApplied.text = "Applied"
            binding.tvApplyNow.visibility = View.GONE
        }
        if (jobsResponse.job?.isJobSaved == 0) {
            binding.tvSaved.text = "Save"
        } else {
            binding.tvSaved.text = "Saved"
        }

        binding.tvSaved.setOnClickListener {
            if (jobsResponse.job?.isJobSaved == 0) {
                saveJob()
            }
        }

        binding.tvApplied.setOnClickListener {
            if (jobsResponse.job?.isJobApplied == 0) {
                applyJob()
            }
        }
    }

    private fun addJobLocation(tittle: String?, desc: String?, orientation: Int): View {
        return JobLayoutDetail(
            requireContext(),
            tittle,
            desc,
            orientation,
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



