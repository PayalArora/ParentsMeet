package com.logicsquare.parentsmeet.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.ui.adapter.KidsAdapter
import com.logicsquare.parentsmeet.databinding.ActivityDrawerBinding
import com.logicsquare.parentsmeet.databinding.AddKidBottomSheetBinding
import com.logicsquare.parentsmeet.model.AddKidRequest
import com.logicsquare.parentsmeet.model.AddKidsResponse
import com.logicsquare.parentsmeet.model.KidsItem
import com.logicsquare.parentsmeet.model.ProfileResponse
import com.logicsquare.parentsmeet.network.APIClient
import com.logicsquare.parentsmeet.network.APIInterface
import com.logicsquare.parentsmeet.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DrawerActivity : AppCompatActivity(), KidsAdapter.OnItemClickListener {

    private lateinit var binding: ActivityDrawerBinding
    private lateinit var bottomSheetBinding: AddKidBottomSheetBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var gender: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ivAdd.setOnClickListener {
            showBottomSheetDialog()
        }

        binding.tvAdd.setOnClickListener {
            showBottomSheetDialog()
        }

        binding.layoutFooter.tvSettings.setOnClickListener {
            val intent = Intent( this,DashboardActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
        binding.layoutFooter.tvSignOut.setOnClickListener {
logoutPopUp()
        }
        bottomSheetDialog = BottomSheetDialog(this)
        getProfile(false)
    }

    private fun logoutPopUp() {
        AlertDialog.Builder(this)
            .setTitle(R.string.app_name)
            .setMessage(R.string.logoutMessage)
            .setIcon(R.mipmap.ic_launcher)
            .setPositiveButton(android.R.string.yes) { dialog, _ ->
                dialog.dismiss()
                val intent = Intent( this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finishAffinity()            }
            .setNegativeButton(android.R.string.no, null).show()
    }
    private fun showBottomSheetDialog() {

        bottomSheetBinding = AddKidBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()

        bottomSheetBinding.llAddKid.visibility = VISIBLE
        bottomSheetBinding.llViewKid.visibility = GONE

        setSpinnerAdapter()
        initListener()
    }

    private fun getProfile(isFromBottomSheet: Boolean) {

        if (isFromBottomSheet)
            bottomSheetBinding.progressBar.visible()
        else
            binding.progressBar.visible()

        val token = "Bearer ${SharedPref(this).getToken()}"
        val call: Call<ProfileResponse?> =
            APIClient.client.create(APIInterface::class.java).getProfile(token)
        call.enqueue(object : Callback<ProfileResponse?> {
            override fun onResponse(
                call: Call<ProfileResponse?>,
                response: Response<ProfileResponse?>,
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null)
                        handleResponse(response.body()!!)
                } else{
                    handleErrorResponse(response.errorBody(), this@DrawerActivity)
                }
                if (isFromBottomSheet) {
                    bottomSheetBinding.progressBar.gone()
                } else {
                    binding.progressBar.gone()
                }

            }

            override fun onFailure(call: Call<ProfileResponse?>, t: Throwable) {
                if (isFromBottomSheet) {
                    bottomSheetBinding.progressBar.gone()
                } else {
                    binding.progressBar.gone()
                }
                showToast(t.localizedMessage)
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun handleResponse(profileResponse: ProfileResponse) {
        binding.tvName.text =
            "${profileResponse.user?.name?.first?.toUpperCas()} ${profileResponse.user?.name?.last?.toUpperCas()}"
        binding.tvEmail.text = profileResponse.user?.email
        setKidsdata(profileResponse)
    }

    fun initListener() {
        bottomSheetBinding.edtGrade.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addKid()
                true
            } else false
        })

        bottomSheetBinding.btnAddKid.setOnClickListener {
            addKid()
        }
    }

    private fun addKid() {
        bottomSheetBinding.btnAddKid.visibility = VISIBLE
        bottomSheetBinding.progressBar.visible()
        val token = "Bearer ${SharedPref(this).getToken()}"
        var addKidRequest = AddKidRequest()
        addKidRequest.age = bottomSheetBinding.edtAge.text.toString()
        addKidRequest.name = bottomSheetBinding.edtName.text.toString()
        addKidRequest.grade = bottomSheetBinding.edtGrade.text.toString()
        addKidRequest.genderPronouns = gender
//        addKidRequest.gender = bottomSheetBinding.edtGender.text.toString()

        val call: Call<AddKidsResponse?> =
            APIClient.client.create(APIInterface::class.java).addKid(token, addKidRequest)
        call.enqueue(object : Callback<AddKidsResponse?> {
            override fun onResponse(
                call: Call<AddKidsResponse?>,
                response: Response<AddKidsResponse?>,
            ) {
                if (response.isSuccessful) {
                    if (response.body()?.error!! && !response.body()?.reason.isNullOrEmpty()) {
                        showToast(response.body()?.reason)
                    } else {
                        getProfile(true)
                        bottomSheetDialog.dismiss()
                    }
                } else {
                    handleErrorResponse(response.errorBody(), this@DrawerActivity)
                }
                bottomSheetBinding.progressBar.gone()
            }

            override fun onFailure(call: Call<AddKidsResponse?>, t: Throwable) {
                bottomSheetBinding.progressBar.gone()
                showToast(t.localizedMessage)
            }
        })
    }

    private fun setSpinnerAdapter() {
        val others = resources.getStringArray(R.array.Gender)
        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_text_gender, others
        )
        bottomSheetBinding.spinnerOther.adapter = adapter

        bottomSheetBinding.spinnerOther.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long,
                ) {
                    var textView: TextView = view.findViewById(R.id.text1)
                    textView.setTextColor(view.context.getColor(R.color.gray_2))
                    gender = others[position].toLowerCase()

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
//        bottomSheetBinding.spinnerOther.setPopupBackgroundDrawable(getDrawable(R.drawable.spinner))
    }

    private fun showKidsData(kidsItem: KidsItem) {
        bottomSheetBinding.btnAddKid.visibility = GONE
        bottomSheetBinding.llAddKid.visibility = GONE
        bottomSheetBinding.llViewKid.visibility = VISIBLE

        bottomSheetBinding.tvName.text = kidsItem.name?.toUpperCas()
        bottomSheetBinding.tvAge.text = kidsItem.age
        bottomSheetBinding.tvGender.text = kidsItem.genderPronouns?.toUpperCas()
        bottomSheetBinding.tvGrade.text = kidsItem.grade
    }

    private fun setKidsdata(profileResponse: ProfileResponse) {

        var kidsAdapter = KidsAdapter(profileResponse.user?.kids as ArrayList<KidsItem>, this)
        kidsAdapter.setListener(this)
        binding.rvChild.adapter = kidsAdapter
    }

    override fun onClick(kidsItem: KidsItem) {
        showBottomSheetDialog()
        showKidsData(kidsItem)
    }
}