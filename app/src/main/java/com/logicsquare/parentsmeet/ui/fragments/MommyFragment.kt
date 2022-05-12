package com.logicsquare.parentsmeet.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.databinding.FragmentMommyBinding

class MommyFragment : Fragment() {


    var oldPosition = R.string.blogs
    var navController: NavController? = null
    lateinit var binding: FragmentMommyBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }


    private fun loadFragment(fragment: Fragment,tittle:String) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.add(R.id.nav_host_fragment, fragment)
//        transaction?.addToBackStack(tittle)
        transaction?.commit()
    }

    private fun initListeners() {
        binding.tvBlogs.setOnClickListener {
            binding.tvBlogs.setBackgroundResource(R.drawable.bg_left_curve_selected)
            binding.tvForm.setBackgroundResource(R.color.white)
            binding.tvJob.setBackgroundResource(R.drawable.bg_right_curve)
            loadFragment(BlogsFragment(),getString(R.string.blogs))
            oldPosition = R.string.blogs
        }
        binding.tvForm.setOnClickListener {
            binding.tvBlogs.setBackgroundResource(R.drawable.bg_left_curve)
            binding.tvForm.setBackgroundResource(R.color.silver_7)
            binding.tvJob.setBackgroundResource(R.drawable.bg_right_curve)
            loadFragment(FormFragment(),getString(R.string.form))
            oldPosition = R.string.form
        }
        binding.tvJob.setOnClickListener {
            binding.tvBlogs.setBackgroundResource(R.drawable.bg_left_curve)
            binding.tvForm.setBackgroundResource(R.color.white)
            binding.tvJob.setBackgroundResource(R.drawable.bg_right_curve_selected)
            loadFragment(JobFragment(),getString(R.string.job))
            oldPosition = R.string.job
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMommyBinding.inflate(layoutInflater)
        return binding.root
    }
}