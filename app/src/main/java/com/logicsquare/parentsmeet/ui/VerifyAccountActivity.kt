package com.logicsquare.parentsmeet.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.databinding.ActivityVerifyAccountBinding
import com.logicsquare.parentsmeet.model.LoginResponse
import com.logicsquare.parentsmeet.model.OtpRequest
import com.logicsquare.parentsmeet.model.SubmitOtpRequest
import com.logicsquare.parentsmeet.network.APIClient
import com.logicsquare.parentsmeet.network.APIInterface
import com.logicsquare.parentsmeet.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VerifyAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifyAccountBinding
    private lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = SharedPref(this)
        getOtp()
        initEditText()
        setListener()
    }

    private fun initEditText() {
        binding.edtCode1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length == 1) {
                    binding.edtCode2.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
            }
        })

        binding.edtCode2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length == 1) {
                    binding.edtCode3.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
            }
        })

        binding.edtCode3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length == 1) {
                    binding.edtCode4.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
            }
        })

        binding.edtCode4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length == 1) {
                    binding.edtCode5.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
            }
        })

        binding.edtCode5.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length == 1) {
                    binding.edtCode6.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
            }
        })
    }

    private fun setListener() {
        binding.btnVerify.setOnClickListener {
            if (validateData())
                submitOtp()
        }
    }

    private fun validateData(): Boolean {
        if (binding.edtCode1.text.toString().isNullOrEmpty() ||
            binding.edtCode2.text.toString().isNullOrEmpty() ||
            binding.edtCode3.text.toString().isNullOrEmpty() ||
            binding.edtCode4.text.toString().isNullOrEmpty() ||
            binding.edtCode5.text.toString().isNullOrEmpty() ||
            binding.edtCode6.text.toString().isNullOrEmpty()
        ) {
            showToast(getString(R.string.err_invalid_code))
            return false
        }

        return true
    }

    private fun submitOtp(){
        var submitOtpRequest = SubmitOtpRequest()
        submitOtpRequest.otp = binding.edtCode1.text.toString().plus(binding.edtCode2.text.toString())
            .plus(binding.edtCode3.text.toString()).plus(binding.edtCode4.text.toString())
            .plus(binding.edtCode5.text.toString()).plus(binding.edtCode6.text.toString())
        submitOtpRequest.handle = intent.getStringExtra("email")!!
        submitOtpRequest.sendTo = "email"

        binding.progressBar.visible()

        val call: Call<LoginResponse?> =
            APIClient.client.create(APIInterface::class.java).submitOtp(
                submitOtpRequest
            )
        call.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                if (response.isSuccessful) {
                    sharedPref.apply {
                        saveUser(response.body()?.user)
                        saveToken(response.body()?.token)
                    }
                    startActivity(Intent(this@VerifyAccountActivity, DashboardActivity::class.java))
                } else {
                    handleErrorResponse(response.errorBody(), this@VerifyAccountActivity)
                }
                binding.progressBar.gone()
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                binding.progressBar.gone()
                showToast(t.localizedMessage)
            }
        })
    }

    private fun getOtp() {
        var otpRequest = OtpRequest()
        otpRequest.sendTo = "email"
        otpRequest.handle = intent.getStringExtra("email")!!

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
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                binding.progressBar.gone()
                showToast(t.localizedMessage)
            }
        })
    }

}