package com.logicsquare.parentsmeet.ui.fragments

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.logicsquare.parentsmeet.databinding.JobDetailBinding

class JobLayoutDetail(@get:JvmName("context") private val context: Context,private val tittle: String?, private val desc: String?, val orientation: Int?):LinearLayout(context) {


    lateinit var binding:JobDetailBinding

    init {
        initView()
    }


    private fun initView() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = JobDetailBinding.inflate(inflater)

        addView(binding.root)

        binding.tvTitle.text = tittle
        /*TODO remove HTML tags for spacing issues */

        binding.llContainer.orientation = orientation!!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvDesc.text = Html.fromHtml(desc, Html.FROM_HTML_MODE_COMPACT)
        } else {
            binding.tvDesc.text = Html.fromHtml(desc)
        }

    }
}