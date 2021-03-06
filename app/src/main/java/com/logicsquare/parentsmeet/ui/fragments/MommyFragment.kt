package com.logicsquare.parentsmeet.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.text.toLowerCase
import androidx.fragment.app.Fragment
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.databinding.FragmentMommyBinding
import com.logicsquare.parentsmeet.utils.SharedPref
import com.logicsquare.parentsmeet.utils.toUpperCas

 var tabPosition = 0
class MommyFragment : Fragment() {
    lateinit var sharedPref: SharedPref

    var oldPosition = R.string.blogs
    lateinit var binding: FragmentMommyBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


    private fun loadFragment(fragment: Fragment,tittle:String) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.nav_host_fragment, fragment)
       // transaction?.addToBackStack(tittle)
        transaction?.commit()
    }

    private fun initListeners() {
        binding.tvBlogs.setOnClickListener {
            tabPosition = 0
            binding.tvBlogs.setBackgroundResource(R.drawable.bg_left_curve_selected)
            binding.tvForm.setBackgroundResource(R.color.white)
            binding.tvJob.setBackgroundResource(R.drawable.bg_right_curve)
            loadFragment(BlogsFragment(),getString(R.string.blogs))
            oldPosition = R.string.blogs
        }
        binding.tvForm.setOnClickListener {
            tabPosition = 1
            binding.tvBlogs.setBackgroundResource(R.drawable.bg_left_curve)
            binding.tvForm.setBackgroundResource(R.color.silver_7)
            binding.tvJob.setBackgroundResource(R.drawable.bg_right_curve)
            loadFragment(ForumFragment(),getString(R.string.form))
            oldPosition = R.string.form
        }
        binding.tvJob.setOnClickListener {
            tabPosition = 2
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
        sharedPref = SharedPref(requireActivity())
        if (sharedPref.getUserRelation()?.toLowerCase().equals(getString(R.string.mom))){
            binding.toolbarText.setText(getString(R.string.mommy).toUpperCas())
        } else if (sharedPref.getUserRelation()?.toLowerCase().equals(getString(R.string.dad).toLowerCase())) {
            binding.toolbarText.setText(getString(R.string.dad).toUpperCas())
        } else if (sharedPref.getUserRelation()?.toLowerCase().equals(getString(R.string.Uncle).toLowerCase())) {
            binding.toolbarText.setText(getString(R.string.Uncle).toUpperCas())
        }  else if (sharedPref.getUserRelation()?.toLowerCase().equals(getString(R.string.Aunty).toLowerCase())) {
            binding.toolbarText.setText(getString(R.string.Aunty).toUpperCas())
        }
        else if (sharedPref.getUserRelation()?.toLowerCase().equals(getString(R.string.grandFather).toLowerCase())) {
            binding.toolbarText.setText(getString(R.string.GrandFather).toUpperCas())
        }
        else if (sharedPref.getUserRelation()?.toLowerCase().equals(getString(R.string.grandMother).toLowerCase())) {
            binding.toolbarText.setText(getString(R.string.GrandMother).toUpperCas())
        }
        initListeners()
        if (tabPosition == 0) {
            binding.tvBlogs.performClick()
        } else if(tabPosition == 1) {
            binding.tvForm.performClick()
        }
        else if(tabPosition == 2) {
            binding.tvJob.performClick()
        }
        return binding.root
    }
}