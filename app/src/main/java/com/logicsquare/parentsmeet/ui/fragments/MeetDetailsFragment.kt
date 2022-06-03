package com.logicsquare.parentsmeet.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.databinding.FragmentMeetBinding
import com.logicsquare.parentsmeet.databinding.FragmentMeetDetailsBinding


class MeetDetailsFragment : Fragment() {


    lateinit var binding:FragmentMeetDetailsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMeetDetailsBinding.inflate(layoutInflater)
        return binding.root
    }
}