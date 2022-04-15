package com.logicsquare.parentsmeet.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.logicsquare.parentsmeet.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity(){
    lateinit var binding:ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}