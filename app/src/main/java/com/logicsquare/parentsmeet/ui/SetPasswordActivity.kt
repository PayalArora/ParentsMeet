package com.logicsquare.parentsmeet.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.databinding.SetPasswordActivityBinding
import com.logicsquare.parentsmeet.model.LoginResponse
import com.logicsquare.parentsmeet.model.SubmitOtpRequest
import com.logicsquare.parentsmeet.network.APIClient
import com.logicsquare.parentsmeet.network.APIInterface
import com.logicsquare.parentsmeet.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: SetPasswordActivityBinding
    private lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SetPasswordActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = SharedPref(this)
        setListener()
    }

    private fun setListener() {
        binding.btnResetPassword.setOnClickListener {
            if (validateData())
                submitOtp()
        }
    }

    private fun validateData(): Boolean {
        if (binding.etPassword.getText().isNullOrEmpty()) {
            showToast(getString(R.string.err_invalid_password))
            return false
        } else if (binding.etRetypePassword.getText()
                .isNullOrEmpty() || binding.etPassword.getText() != binding.etRetypePassword.getText()
        ) {
            showToast(getString(R.string.err_invalid_retyp_password))
            return false
        }

        return true
    }

    private fun submitOtp() {
        var submitOtpRequest = SubmitOtpRequest()
        submitOtpRequest.otp = intent.getStringExtra("otp")!!
        submitOtpRequest.handle = intent.getStringExtra("email")!!
        submitOtpRequest.sendTo = intent.getStringExtra("sendto")!!
        submitOtpRequest.isResetPassword = true
        submitOtpRequest.password = binding.etPassword.getText()

        binding.progressBar.visible()

        val call: Call<LoginResponse?> =
            APIClient.client.create(APIInterface::class.java).submitOtp(
                submitOtpRequest
            )
        call.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>,
            ) {
                binding.progressBar.gone()
                if (response.isSuccessful) {
                    startActivity(Intent(this@SetPasswordActivity, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
                } else {
                    handleErrorResponse(response.errorBody(), this@SetPasswordActivity)
                    finish()
                }
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                binding.progressBar.gone()
                showToast(t.localizedMessage)
            }
        })
    }

}