package com.logicsquare.parentsmeet.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.adapter.ARG_OBJECT
import com.logicsquare.parentsmeet.adapter.ChildPagerAdapter
import com.logicsquare.parentsmeet.databinding.SettingsChildBinding

class ChildPagerFragment  : Fragment() {
    private lateinit var mBinding: SettingsChildBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        mBinding = SettingsChildBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
           //mBinding.childName.text= getInt(ARG_OBJECT).toString()
        }
    }
}