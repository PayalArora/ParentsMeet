package com.logicsquare.parentsmeet.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.databinding.ForgotPasswordActivityBinding
import com.logicsquare.parentsmeet.model.LoginResponse
import com.logicsquare.parentsmeet.model.OtpRequest
import com.logicsquare.parentsmeet.network.APIClient
import com.logicsquare.parentsmeet.network.APIInterface
import com.logicsquare.parentsmeet.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ForgotPasswordActivityBinding
    private lateinit var sharedPref: SharedPref
    var mSendTo = "email"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ForgotPasswordActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = SharedPref(this)
        setListener()
    }

    private fun setListener() {
        binding.btnResetPassword.setOnClickListener {
            if (validateData()) {
                var otpRequest = OtpRequest()
                otpRequest.sendTo = mSendTo
                otpRequest.handle = binding.txtEmail.text.toString().trim()
                getOtp(otpRequest)
            }
        }
    }

    private fun validateData(): Boolean {
        if (binding.txtEmail.text.toString().isNullOrEmpty()
        ) {
            showToast(getString(R.string.err_invalid_email_phone))
            return false
        } else if (isEmailIdValid(binding.txtEmail.text.toString()))
        {
            mSendTo = "email"
            return true
        } else if (android.util.Patterns.PHONE.matcher(binding.txtEmail.text.toString()).matches()){
            mSendTo = "phone"
            return true
        } else{
            showToast(getString(R.string.err_invalid_email_phone))
        }
        return false
    }


    private fun getOtp(otpRequest: OtpRequest) {

        binding.progressBar.visible()

        val call: Call<LoginResponse?> =
            APIClient.client.create(APIInterface::class.java).getOtp(
                otpRequest
            )
        call.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                binding.progressBar.gone()
                if (response.isSuccessful) {
                    var intent = Intent(this@ForgotPasswordActivity, ResetPasswordActivity::class.java)
                    intent.putExtra("email", binding.txtEmail.text.toString())
                    intent.putExtra("sendto",mSendTo)
                    startActivity(intent)
                } else {
                    handleErrorResponse(response.errorBody(), this@ForgotPasswordActivity)
                }
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                binding.progressBar.gone()
                showToast(t.localizedMessage)
            }
        })
    }

}