package com.logicsquare.parentsmeet.ui

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.databinding.ActivitySignupBinding
import com.logicsquare.parentsmeet.model.LoginResponse
import com.logicsquare.parentsmeet.model.SignUpRequest
import com.logicsquare.parentsmeet.network.APIClient
import com.logicsquare.parentsmeet.network.APIInterface
import com.logicsquare.parentsmeet.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class SignUpActivity : AppCompatActivity(), OnDateSetListener {

    private lateinit var sharedPref: SharedPref
    private lateinit var binding: ActivitySignupBinding
    private var relation: String = ""
    private val myCalendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = SharedPref(this)
        setSpinnerAdapter()
        setListener()
    }

    private fun setSpinnerAdapter() {
        val others = resources.getStringArray(R.array.Others)
        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_text_view, others
        )
        binding.spinnerOther.adapter = adapter

        binding.spinnerOther.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long,
            ) {
                if (position > 0) {
                    relation = others[position]
                    var textView:TextView = view.findViewById(R.id.text1)
                    textView.setTextColor(view.context.getColor(R.color.blue_1))
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    private fun setDob() {
        val myFormat = "MM/dd/yy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        binding.edtDob.setText(dateFormat.format(myCalendar.time))
    }

    private fun setListener() {
        binding.btnSignIn.setOnClickListener {
            if (validateData())
                signIn()
        }

        binding.ivBack.setOnClickListener {
           finish()
        }

        binding.edtDob.setOnClickListener {
            val dialog = DatePickerDialog(
                this, this,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            )
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 568025136000L);
            dialog.show()

        }

        binding.ivDad.setOnClickListener {
            relation = getString(R.string.dad).toLowerCase()
            binding.tvDad.setTextColor(this.getColor(R.color.blue_1))
            binding.tvMom.setTextColor(this.getColor(R.color.black))
            binding.spinnerOther.setSelection(0)
        }
        binding.ivMom.setOnClickListener {
            relation = getString(R.string.mom).toLowerCase()
            binding.tvMom.setTextColor(this.getColor(R.color.blue_1))
            binding.tvDad.setTextColor(this.getColor(R.color.black))
            binding.spinnerOther.setSelection(0)
        }

    }

    private fun validateData(): Boolean {
        if (binding.edtFName.getText().isNullOrEmpty()) {
            showToast(getString(R.string.err_invalid_first_name))
            return false
        } else if (binding.edtLName.getText().isNullOrEmpty()) {
            showToast(getString(R.string.err_invalid_last_name))
            return false
        } else if (binding.etEmailId.getText()
                .isBlank() || !isEmailIdValid(binding.etEmailId.getText())
        ) {
            showToast(getString(R.string.err_invalid_email_id))
            return false
        } else if (binding.edtCellphone.getText().isNullOrEmpty()) {
            showToast(getString(R.string.err_invalid_cellphone))
            return false
        } else if (binding.etPassword.getText().isNullOrEmpty() || binding.edtConfirmPwd.getText()
                .isNullOrEmpty()
        ) {
            showToast(getString(R.string.err_invalid_password))
            return false
        } else if (binding.etPassword.getText() != binding.edtConfirmPwd.getText()) {
            showToast(getString(R.string.err_invalid_password))
            return false
        }
        return true
    }

    private fun signIn() {
        val signUpRequest = SignUpRequest()
        signUpRequest.name.first = binding.edtFName.getText()
        signUpRequest.name.last = binding.edtLName.getText()
        signUpRequest.email = binding.etEmailId.getText()
        signUpRequest.phoneCountryCode = "+91"
        signUpRequest.phone = binding.edtCellphone.getText()
        signUpRequest.password = binding.etPassword.getText()
        signUpRequest.relation = relation
        signUpRequest.deviceDetails.name = Build.BRAND
        signUpRequest.deviceDetails.deviceId = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )
        signUpRequest.allowNotification = false
        signUpRequest.dob.day = myCalendar.time.day.toString()
        signUpRequest.dob.month = myCalendar.time.month.toString()
        signUpRequest.dob.year = myCalendar.time.year.toString()

        binding.progressBar.visible()
        val call: Call<LoginResponse?> = APIClient.client.create(APIInterface::class.java).signUp(
            signUpRequest
        )
        call.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>,
            ) {
                if (response.isSuccessful) {
                    if (response.body()?.error!! && !response.body()?.reason.isNullOrEmpty()) {
                        showToast(response.body()?.reason)
                    }
                    handleResponse(response)
                } else{
                    handleErrorResponse(response.errorBody(), this@SignUpActivity)
                }
                binding.progressBar.gone()
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                binding.progressBar.gone()
                showToast(t.localizedMessage)
            }
        })
    }

    private fun handleResponse(response: Response<LoginResponse?>) {
        if (response.isSuccessful) {
            var intent = Intent(this@SignUpActivity, VerifyAccountActivity::class.java)
            intent.putExtra("email", binding.etEmailId.getText())
            startActivity(intent)
            finish()
        } else {
            handleErrorResponse(response.errorBody(), this@SignUpActivity)
        }
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, month)
        myCalendar.set(Calendar.DAY_OF_MONTH, day)
        setDob()
    }
}