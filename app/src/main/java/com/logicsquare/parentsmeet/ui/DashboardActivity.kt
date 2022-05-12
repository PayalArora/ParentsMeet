package com.logicsquare.parentsmeet.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.databinding.ActivityDashboardBinding
import com.logicsquare.parentsmeet.ui.fragments.ForumFragment
import com.logicsquare.parentsmeet.ui.fragments.SettingsFragment

class DashboardActivity : AppCompatActivity(){
    lateinit var binding:ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.navigationView.menu.getItem(0).isCheckable=false
        loadFragment(SettingsFragment())
        binding.toolbar.toolbarText.text = getString(R.string.settings)
        binding.toolbar.menuOption.setOnClickListener { startActivity(Intent(this@DashboardActivity, DrawerActivity::class.java)) }
        setListeners()
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }

    private fun setListeners() {
        binding.navigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_mommy -> {
                    binding.navigationView.menu.getItem(0).isCheckable=true
                    loadFragment(ForumFragment())
                }
                R.id.menu_calender -> {

                }
                R.id.menu_kids -> {

                }
                R.id.menu_meet -> {

                }
                R.id.menu_messages -> {

                }
            }
            true
        }
    }

}