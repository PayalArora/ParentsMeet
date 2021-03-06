package com.logicsquare.parentsmeet.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.logicsquare.parentsmeet.model.Kid
import com.logicsquare.parentsmeet.model.KidsItem
import com.logicsquare.parentsmeet.model.SettingResponse
import com.logicsquare.parentsmeet.ui.fragments.ChildPagerFragment
import com.logicsquare.parentsmeet.ui.fragments.KidsData


class ChildPagerAdapter(fragment: Fragment, val kidsList: List<KidsItem?>?, val kidsdata: KidsData, val settingResponse: SettingResponse?) : FragmentStateAdapter(fragment), KidsData {

    override fun getItemCount(): Int = kidsList?.size?:0

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        val fragment = ChildPagerFragment(kidsList?.get(position), this, settingResponse
        )
//        fragment.arguments = Bundle().apply {
//            // Our object is just an integer :-P
//            putInt(ARG_OBJECT, position + 1)
//        }
        return fragment
    }

    override fun kidsData(kid: Kid?) {
        kidsdata.kidsData(kid)
    }
}

 const val ARG_OBJECT = "child"
