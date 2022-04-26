package com.logicsquare.parentsmeet.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.databinding.ResetPasswordBinding
import com.logicsquare.parentsmeet.utils.SharedPref
import com.logicsquare.parentsmeet.utils.showToast


class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ResetPasswordBinding
    private lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = SharedPref(this)
        initEditText()
        setListener()
    }

    private fun setListener() {
        binding.btnResetPassword.setOnClickListener {
            if (validateData()){
                var intent = Intent(this@ResetPasswordActivity, SetPasswordActivity::class.java)
                intent.putExtra("email", getIntent().getStringExtra("email")!!)
                intent.putExtra("sendto", getIntent().getStringExtra("sendto")!!)
                val otp = binding.edtCode1.text.toString().plus(binding.edtCode2.text.toString())
                    .plus(binding.edtCode3.text.toString()).plus(binding.edtCode4.text.toString())
                    .plus(binding.edtCode5.text.toString()).plus(binding.edtCode6.text.toString())
                intent.putExtra("otp", otp)
                startActivity(intent)
            }
        }
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
}