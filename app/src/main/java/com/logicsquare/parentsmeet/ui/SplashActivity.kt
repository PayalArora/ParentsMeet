package com.logicsquare.parentsmeet.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.utils.SharedPref

class SplashActivity : AppCompatActivity() {
    lateinit var sharedPref: SharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = SharedPref(this)
        if (SharedPref(this).getKeepMe()) {
            startActivity(Intent(this, DashboardActivity::class.java))
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
            }, SPLASH_TIMEOUT)
        }
    }
companion object {
    const val SPLASH_TIMEOUT = 2000L
}

}
