package com.logicsquare.parentsmeet.ui

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.logicsquare.parentsmeet.databinding.MeetDetailContainerBinding

class MeetDetailModule (@get:JvmName("context") private val context: Context, private val leftTittle: String?,private val rightTittle: String?, private val leftDesc: String?, private val rightDesc: String?):
    LinearLayout(context) {


    lateinit var binding: MeetDetailContainerBinding

    init {
        initView()
    }


    private fun initView() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = MeetDetailContainerBinding.inflate(inflater)

        addView(binding.root)

        binding.tvLeftTitle.text = leftTittle
        binding.tvLeftDesc.text = leftDesc

        binding.tvRightDesc.text = rightDesc
        binding.tvRightTitle.text = rightTittle
    }
}