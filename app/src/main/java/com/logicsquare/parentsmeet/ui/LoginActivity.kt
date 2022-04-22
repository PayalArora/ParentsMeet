package com.logicsquare.parentsmeet.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.databinding.ActivityLoginBinding
import com.logicsquare.parentsmeet.model.LoginRequest
import com.logicsquare.parentsmeet.model.LoginResponse
import com.logicsquare.parentsmeet.network.APIClient
import com.logicsquare.parentsmeet.network.APIInterface
import com.logicsquare.parentsmeet.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// keytool -exportcert -alias vayco -keystore E:\AndroidStudioProjects\Vayco\releaseWork\vayco.jks | E:\openssl-0.9.8k_WIN32\bin\openssl sha1 -binary | E:\openssl-0.9.8k_WIN32\bin\openssl base64
class LoginActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPref
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = SharedPref(this)
        setListener()
    }

    private fun setListener() {
        binding.btnLogin.setOnClickListener {
            if (isInputValid()) {
                loginWithUserIdPassword()
            }
        }
        binding.txtKeepLogin.setOnClickListener {
            binding.checkKeepme.performClick()
        }

        binding.tvNoAcc.setOnClickListener {
            openSignInActivity()
        }

        binding.btnForgotPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        }
    }

    private fun openSignInActivity() {
        startActivity(Intent(this,SignUpActivity::class.java))
    }

    private fun isInputValid(): Boolean {
        if (binding.etEmailId.getText().isBlank() || !isEmailIdValid(binding.etEmailId.getText())) {
            showToast(getString(R.string.err_invalid_email_id))
            return false
        } else if (binding.etPassword.getText().isBlank()) {
            showToast(getString(R.string.err_invalid_password))
            return false
        }
        return true
    }

    private fun loginWithUserIdPassword() {
        val loginRequest = LoginRequest()
        loginRequest.handle = binding.etEmailId.getText()
        loginRequest.password = binding.etPassword.getText()
        loginRequest.deviceDetails.name = Build.BRAND
        loginRequest.deviceDetails.deviceId = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )
        loginRequest.allowNotification = false
        binding.progressBar.visible()
        val call: Call<LoginResponse?> = APIClient.client.create(APIInterface::class.java).login(
            loginRequest
        )
        call.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                binding.progressBar.gone()
                handleResponse(response)
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                binding.progressBar.gone()
                showToast(t.localizedMessage)
            }
        })
    }


    private fun handleResponse(response: Response<LoginResponse?>) {
        if (response.isSuccessful) {
            sharedPref.apply {
                saveUser(response.body()?.user)
                saveToken(response.body()?.token)
                saveKeepMe(binding.checkKeepme.isChecked)
            }
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
            finish()
        } else {
            handleErrorResponse(response.errorBody(), this@LoginActivity)
        }
    }
}
