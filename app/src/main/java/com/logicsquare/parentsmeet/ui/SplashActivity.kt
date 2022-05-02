package com.logicsquare.parentsmeet.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.logicsquare.parentsmeet.utils.SharedPref

class SplashActivity : AppCompatActivity() {
    lateinit var sharedPref: SharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = SharedPref(this)
        if (SharedPref(this).getKeepMe() && SharedPref(this).isLoggedIn()) {
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
