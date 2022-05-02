package com.logicsquare.parentsmeet.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayoutMediator
import com.logicsquare.parentsmeet.adapter.ChildPagerAdapter
import com.logicsquare.parentsmeet.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private lateinit var mBinding: FragmentSettingsBinding
    private lateinit var childPagerAdapter: ChildPagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSettingsBinding.inflate(inflater, container, false)
        childPagerAdapter = ChildPagerAdapter(this)
        mBinding.pager.adapter = childPagerAdapter
//        mBinding.tabLayout.setupWithViewPager(mBinding.pager)
        TabLayoutMediator(mBinding.tabLayout, mBinding.pager) { tab, position ->
            tab.text = "Child ${(position + 1)}"
        }.attach()
        setListeners()
        return mBinding.root
    }

    private fun setListeners() {
    }


}