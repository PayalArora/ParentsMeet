package com.logicsquare.parentsmeet.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.databinding.ActivityDashboardBinding
import com.logicsquare.parentsmeet.ui.fragments.MommyFragment
import com.logicsquare.parentsmeet.ui.fragments.ForumFragment
import com.logicsquare.parentsmeet.ui.fragments.SettingsFragment
import com.logicsquare.parentsmeet.utils.SharedPref
import com.logicsquare.parentsmeet.utils.toUpperCas

class DashboardActivity : AppCompatActivity(){
    lateinit var sharedPref:SharedPref
    lateinit var binding:ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.navigationView.menu.getItem(0).isCheckable=false
        loadFragment(SettingsFragment())
        binding.toolbar.toolbarText.text = getString(R.string.settings)
        binding.toolbar.menuOption.setOnClickListener { startActivity(Intent(this@DashboardActivity, DrawerActivity::class.java)) }
       sharedPref = SharedPref(this)
        if (sharedPref.getUserRelation()?.toLowerCase().equals(getString(R.string.mom))){
            binding.navigationView.menu.getItem(0).setTitle(getString(R.string.mommy).toUpperCas())
        } else if (sharedPref.getUserRelation()?.toLowerCase().equals(getString(R.string.dad))) {
            binding.navigationView.menu.getItem(0).setTitle(getString(R.string.dad).toUpperCas())
        } else if (sharedPref.getUserRelation()?.toLowerCase().equals(getString(R.string.Uncle))) {
            binding.navigationView.menu.getItem(0).setTitle(getString(R.string.Uncle).toUpperCas())
        }  else if (sharedPref.getUserRelation()?.toLowerCase().equals(getString(R.string.Aunty))) {
            binding.navigationView.menu.getItem(0).setTitle(getString(R.string.Aunty).toUpperCas())
        }
        else if (sharedPref.getUserRelation()?.toLowerCase().equals(getString(R.string.grandFather))) {
            binding.navigationView.menu.getItem(0).setTitle(getString(R.string.GrandFather).toUpperCas())
        }
        else if (sharedPref.getUserRelation()?.toLowerCase().equals(getString(R.string.grandMother))) {
            binding.navigationView.menu.getItem(0).setTitle(getString(R.string.GrandMother).toUpperCas())
        }
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

                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.container, MommyFragment())
                    transaction.addToBackStack("Mommy Fragment")
                    transaction.commit()

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