package com.logicsquare.parentsmeet.ui

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class SignUpActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPref
    private lateinit var binding: ActivitySignupBinding
    private var relation: String = "mom"
    private val myCalendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = SharedPref(this)

        binding.tvMom.setTextColor(this.getColor(R.color.blue_1))
        setSpinnerAdapter()
        binding.spinnerLayout.setOnClickListener {
            binding.spinnerOther.performClick()
        }
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
                var textView: TextView = view.findViewById(R.id.text1)
                textView.setTextColor(view.context.getColor(R.color.gray_2))
                if (position > 0) {
                    if (position == 1){
                        relation == "uncle"
                    } else if  (position == 2){
                        relation == "aunty"
                    } else if (position == 3){
                        relation == "grandFather"
                    } else if (position == 4){
                        relation == "grandMother"
                    }
                    var textView: TextView = view.findViewById(R.id.text1)
                    textView.setTextColor(view.context.getColor(R.color.blue_1))
                    binding.tvMom.setTextColor(view.context.getColor(R.color.gray_2))
                    binding.tvDad.setTextColor(view.context.getColor(R.color.gray_2))
                } else if (!relation.equals("dad", true)) {
                    binding.tvMom.setTextColor(view.context.getColor(R.color.blue_1))
                }  else if(relation.equals("dad", true)){
                    binding.tvDad.setTextColor(view.context.getColor(R.color.blue_1))
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
        binding.spinnerOther.setPopupBackgroundDrawable(getDrawable(R.drawable.spinner))
    }

//    private fun setDob() {
//        val myFormat = "MM/dd/yyyy"
//        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
//        binding.edtDob.setText(dateFormat.format(myCalendar.time))
//    }

    private fun setListener() {
        binding.btnSignIn.setOnClickListener {
            if (validateData()) {
                signIn()
            }
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

        binding.ivBack.setOnClickListener {
            finish()
        }

        //binding.edtDob.setOnClickListener {
//            val dialog = DatePickerDialog(
//                this, this,
//                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                myCalendar.get(Calendar.DAY_OF_MONTH)
//            )
//            dialog.datePicker.maxDate = System.currentTimeMillis() - 568025136000L;
//            dialog.show()

      //  }

        binding.ivDad.setOnClickListener {
            relation = getString(R.string.dad).toLowerCase()
            binding.tvDad.setTextColor(this.getColor(R.color.blue_1))
            binding.tvMom.setTextColor(this.getColor(R.color.gray_2))
            binding.spinnerOther.setSelection(0)
        }
        binding.ivMom.setOnClickListener {
            relation = getString(R.string.mom).toLowerCase()
            binding.tvMom.setTextColor(this.getColor(R.color.blue_1))
            binding.tvDad.setTextColor(this.getColor(R.color.gray_2))
            binding.spinnerOther.setSelection(0)
        }

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
        if (binding.edtFName.getText().isNullOrEmpty() || binding.edtFName.getText()
                .contains(Regex("\\d+")) || binding.edtFName.getText().length !in 3..24
        ) {
            showToast(getString(R.string.err_invalid_first_name))
            return false
        } else if (binding.edtLName.getText().isNullOrEmpty() || binding.edtLName.getText()
                .contains(Regex("\\d+")) || binding.edtLName.getText().length !in 3..24
        ) {
            showToast(getString(R.string.err_invalid_last_name))
            return false
        }else if (binding.edtDob.getText().isNullOrEmpty() || !checkDate(binding.edtDob.getText().toString())
        ) {
            showToast(getString(R.string.err_invalid_dob))
            return false
        }
        else if (binding.etEmailId.getText()
                .isBlank() || !isEmailIdValid(binding.etEmailId.getText())
        ) {
            showToast(getString(R.string.err_invalid_email_id))
            return false
        } else if (binding.edtCellphone.getText()
                .isNullOrEmpty()
        ) {
            showToast(getString(R.string.err_invalid_cellphone))
            return false
        } else if (binding.etPassword.text.isNullOrEmpty() || binding.edtConfirmPwd.text
                .isNullOrEmpty()
        ) {
            showToast(getString(R.string.err_invalid_password))
            return false
        } else if (binding.etPassword.text.toString() != binding.edtConfirmPwd.text
                .toString() || binding.etPassword.text.length !in 9..19 || !isValidPassword(
                binding.etPassword.text.toString()
            )
        ) {
            showToast(getString(R.string.err_invalid_password))
            return false
        }
        return true
    }

    private fun isValidPassword(password: String): Boolean {
        val pattern: Pattern
        val regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
        pattern = Pattern.compile(regex)
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }

    private fun signIn() {
        val signUpRequest = SignUpRequest()
        signUpRequest.name.first = binding.edtFName.getText()
        signUpRequest.name.last = binding.edtLName.getText()
        signUpRequest.email = binding.etEmailId.getText()
        signUpRequest.phoneCountryCode = "+91"
        signUpRequest.phone = binding.edtCellphone.getText()
        signUpRequest.password = binding.etPassword.getText().toString()
        signUpRequest.relation = relation
        signUpRequest.deviceDetails.name = Build.BRAND
        signUpRequest.deviceDetails.deviceId = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )
        signUpRequest.allowNotification = false
        signUpRequest.dob.day = binding.edtDob.text.toString().split("/")[0]
        signUpRequest.dob.month = binding.edtDob.text.toString().split("/")[1]
        signUpRequest.dob.year = binding.edtDob.text.toString().split("/")[2]

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
                } else {
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

//    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
//        myCalendar.set(Calendar.YEAR, year)
//        myCalendar.set(Calendar.MONTH, month)
//        myCalendar.set(Calendar.DAY_OF_MONTH, day)
//        setDob()
//    }

}

