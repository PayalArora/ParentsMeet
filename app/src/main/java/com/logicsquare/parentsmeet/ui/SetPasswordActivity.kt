package com.logicsquare.parentsmeet.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
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

        binding.etPassword.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (ifContainsDigit(binding.etPassword.text.toString())) {
                    setDrawableLeft(binding.tvDigit, true)
                    setTextColor(binding.tvDigit, R.color.blue_1)
                } else {
                    setDrawableLeft(binding.tvDigit, false)
                    setTextColor(binding.tvDigit, R.color.black)
                }

                if (ifContainsUpperCase(binding.etPassword.text.toString())) {
                    setDrawableLeft(binding.tvUpperCase, true)
                    setTextColor(binding.tvUpperCase, R.color.blue_1)
                } else {
                    setDrawableLeft(binding.tvUpperCase, false)
                    setTextColor(binding.tvUpperCase, R.color.black)
                }

                if (ifContainsLowerCase(binding.etPassword.text.toString())) {
                    setDrawableLeft(binding.tvLowerCase, true)
                    setTextColor(binding.tvLowerCase, R.color.blue_1)
                } else {
                    setDrawableLeft(binding.tvLowerCase, false)
                    setTextColor(binding.tvLowerCase, R.color.black)
                }

                if (ifContainsSpecialChar(binding.etPassword.text.toString())) {
                    setDrawableLeft(binding.tvSpecialChar, true)
                    setTextColor(binding.tvSpecialChar, R.color.blue_1)
                } else {
                    setDrawableLeft(binding.tvSpecialChar, false)
                    setTextColor(binding.tvSpecialChar, R.color.black)
                }
            }
        })
    }


    private fun setDrawableLeft(textView: TextView, showEnabled: Boolean) {
        if (showEnabled) {
            textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.dot_selected,
                0,
                0,
                0
            )
        } else {
            textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.dot,
                0,
                0,
                0
            )
        }
    }

    private fun setTextColor(textView: TextView, color: Int) {
        textView.setTextColor(getColor(color))
    }

    private fun validateData(): Boolean {
        if (binding.etPassword.getText().isNullOrEmpty()) {
            showToast(getString(R.string.err_invalid_password))
            return false
        } else if (binding.etRetypePassword.getText()
                .isNullOrEmpty() || binding.etPassword.text.toString() != binding.etRetypePassword.getText()
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
        submitOtpRequest.password = binding.etPassword.text.toString()

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