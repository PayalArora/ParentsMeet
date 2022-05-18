package com.logicsquare.parentsmeet.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.databinding.FragmentJobBinding
import com.logicsquare.parentsmeet.model.JobAppliedSavedResponse
import com.logicsquare.parentsmeet.model.JobsItem
import com.logicsquare.parentsmeet.model.JobsResponse
import com.logicsquare.parentsmeet.network.APIClient
import com.logicsquare.parentsmeet.network.APIInterface
import com.logicsquare.parentsmeet.ui.JobsAdapter
import com.logicsquare.parentsmeet.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobFragment : Fragment(), JobsAdapter.OnItemClickListener {

    private lateinit var binding: FragmentJobBinding

    private var category: String = "all"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvApplied.setOnClickListener{
            binding.tvApplied.setBackgroundResource(R.drawable.background_normal_button)
            binding.tvSaved.setBackgroundResource(R.drawable.background_gradient)
            binding.tvAll.setBackgroundResource(R.drawable.background_gradient)

            binding.tvApplied.setTextColor(resources.getColor(R.color.dark_green))
            binding.tvSaved.setTextColor(resources.getColor(R.color.white))
            binding.tvAll.setTextColor(resources.getColor(R.color.white))
            category = "jobApplied"

            getAllJobs()
        }

        binding.tvSaved.setOnClickListener{
            binding.tvSaved.setBackgroundResource(R.drawable.background_normal_button)
            binding.tvApplied.setBackgroundResource(R.drawable.background_gradient)
            binding.tvAll.setBackgroundResource(R.drawable.background_gradient)

            binding.tvApplied.setTextColor(resources.getColor(R.color.white))
            binding.tvSaved.setTextColor(resources.getColor(R.color.dark_green))
            binding.tvAll.setTextColor(resources.getColor(R.color.white))
            category = "jobSaved"

            getAllJobs()
        }

        binding.tvAll.setOnClickListener{
            binding.tvSaved.setBackgroundResource(R.drawable.background_gradient)
            binding.tvApplied.setBackgroundResource(R.drawable.background_gradient)
            binding.tvAll.setBackgroundResource(R.drawable.background_normal_button)

            binding.tvApplied.setTextColor(resources.getColor(R.color.white))
            binding.tvSaved.setTextColor(resources.getColor(R.color.white))
            binding.tvAll.setTextColor(resources.getColor(R.color.dark_green))
            category = "all"

            getAllJobs()
        }

        getAllJobs()
    }

    private fun getAllJobs() {
        val token = "Bearer ${SharedPref(requireContext()).getToken()}"
        val call: Call<JobsResponse?> =
            APIClient.client.create(APIInterface::class.java).getJobs(token, category)
        showProgressBar()
        call.enqueue(object : Callback<JobsResponse?> {
            override fun onResponse(
                call: Call<JobsResponse?>,
                response: Response<JobsResponse?>,
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
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.add(R.id.container, fragment)
        transaction.addToBackStack("JobDetailsFragment")
        transaction.commit()
    }

    override fun onClick(job: JobsItem) {
        loadFragment(JobDetailsFragment(job.jobId!!))
    }

    override fun applyJob(job: JobsItem) {
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

    override fun saveJob(job: JobsItem) {
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
    }
}