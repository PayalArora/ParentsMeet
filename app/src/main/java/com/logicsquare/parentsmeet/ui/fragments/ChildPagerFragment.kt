package com.logicsquare.parentsmeet.ui.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.core.view.contains
import androidx.fragment.app.Fragment
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.databinding.SettingsChildBinding
import com.logicsquare.parentsmeet.model.AddKidRequest
import com.logicsquare.parentsmeet.model.AddKidsResponse
import com.logicsquare.parentsmeet.model.KidsItem
import com.logicsquare.parentsmeet.model.UpdateKidRequest
import com.logicsquare.parentsmeet.network.APIClient
import com.logicsquare.parentsmeet.network.APIInterface
import com.logicsquare.parentsmeet.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChildPagerFragment(val kidsItem: KidsItem?) : Fragment() {
    private lateinit var mBinding: SettingsChildBinding
    var activitiesList = arrayListOf<String>()
    var gamesList = arrayListOf<String>()
    var needsList = arrayListOf<String>()
    private lateinit var colorPickerDialog: ColorPickerDialog
    var colorBar = "#2F8390"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mBinding = SettingsChildBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       colorPickerDialog = ColorPickerDialog
            .Builder(requireContext())                        // Pass Activity Instance
            .setTitle("Colorbar")            // Default "Choose Color"
            .setColorShape(ColorShape.SQAURE)   // Default ColorShape.CIRCLE
            .setDefaultColor(R.color.green_1)   // Pass Default Color
            .setColorListener { color, colorHex ->
                mBinding.view.setBackgroundColor(Color.parseColor(colorHex))
                colorBar = colorHex
            }.build()
        setSpinnerAdapter()
        setListeners()
        kidsItem?.let {

            it.name?.let { if (it.split(" ").size >1 ){
                mBinding.etName.setText(it.split(" ")[0].toUpperCas()+" "+it.split(" ")[1].toUpperCas())
            } else{
                mBinding.etName.setText(it.toUpperCas())
            } }

            mBinding.etAge.setText(it.age)
            mBinding.edtGrade.setText(it.grade)
            it.colorBar?.let { it1 -> mBinding.view.setBackgroundColor(Color.parseColor(it.colorBar)) }
            val others = resources.getStringArray(R.array.Gender)
            if (others.contains(kidsItem.genderPronouns?.toUpperCas())) {
                mBinding.spinnerOther.setSelection(others.indexOf(kidsItem.genderPronouns?.toUpperCas()))
            }
            it.preferences?.activities?.map {
                addNewChip(mBinding.chipActivities,
                    it.toString(),
                    activitiesList)
            }
            it.preferences?.games?.map { addNewChip(mBinding.chipGames, it.toString(), gamesList) }
            it.preferences?.specialNeeds?.map {
                addNewChip(mBinding.chipNeeds,
                    it.toString(),
                    needsList)
            }
        }
    }

    private fun setListeners() {
        mBinding.tagGames.setOnClickListener {
            showDialog(requireContext(), mBinding.chipGames, gamesList)

        }

        mBinding.tagActivities.setOnClickListener {
            //addNewChip(mBinding.chipActivities,"tag", activitiesList)
            showDialog(requireContext(), mBinding.chipActivities, activitiesList)

        }

        mBinding.tagNeeds.setOnClickListener {
            showDialog(requireContext(), mBinding.chipNeeds, needsList)
        }
        mBinding.btnSave.setOnClickListener { updateKid() }
        mBinding.view.setOnClickListener{
            openColorPicker()
        }
    }

    private fun setSpinnerAdapter() {
        val others = resources.getStringArray(R.array.Gender)
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_text_gender, others
        )
        mBinding.spinnerOther.adapter = adapter
        mBinding.spinnerOther.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long,
                ) {
                    var textView: TextView = view.findViewById(R.id.text1)
                    textView.setTextColor(view.context.getColor(R.color.black_2))
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

    }

    fun addNewChip(view: ChipGroup, tag: String, list: ArrayList<String>) {
        val chip = Chip(requireContext())
        val paddingDp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 10f,
            resources.displayMetrics
        ).toInt()
        chip.chipCornerRadius = 5f
        chip.setChipBackgroundColor(resources.getColorStateList(R.color.green_1))
        chip.text = tag.toUpperCas()
        val colorInt = resources.getColor(R.color.white)
        val csl = ColorStateList.valueOf(colorInt)
        chip.setCloseIconTint(csl)
        chip.setTextColor(Color.WHITE)
        chip.setCloseIconEnabled(true)
        if (tag.trim().length > 0 && !list.contains(tag)) {
            list.add(tag.toLowerCase())
            view.addView(chip, view.childCount - 1)
        }
        chip.setOnCloseIconClickListener {
            if (view.contains(chip)) {
                view.removeView(chip)
                if (list.contains(tag)) {
                    list.remove(tag)
                }
            }
        }
    }

    private fun updateKid() {

        val token = "Bearer ${SharedPref(requireContext()).getToken()}"
        var addKidRequest = UpdateKidRequest()
        addKidRequest.age = mBinding.etAge.text.toString()
        addKidRequest.name = mBinding.etName.text.toString()
        addKidRequest.grade = mBinding.edtGrade.text.toString()
        addKidRequest.genderPronouns = mBinding.spinnerOther.selectedItem.toString().toLowerCase()
        addKidRequest.preferences.activities = activitiesList
        addKidRequest.preferences.games = gamesList
        addKidRequest.preferences.specialNeeds = needsList
        addKidRequest.colorBar = colorBar

        val call: Call<AddKidsResponse?> =
            APIClient.client.create(APIInterface::class.java)
                .updateKid(kidsItem?.id, token, addKidRequest)
        showProgressBar()
        call.enqueue(object : Callback<AddKidsResponse?> {
            override fun onResponse(
                call: Call<AddKidsResponse?>,
                response: Response<AddKidsResponse?>,
            ) {
                if (response.isSuccessful) {
                    if (response.body()?.error!! && !response.body()?.reason.isNullOrEmpty()) {
                        showToast(response.body()?.reason)
                    } else {
                        hideProgressBar()
                    }
                } else {
                    hideProgressBar()
                    handleErrorResponse(response.errorBody(), requireContext())
                }

            }

            override fun onFailure(call: Call<AddKidsResponse?>, t: Throwable) {
                hideProgressBar()
                showToast(t.localizedMessage)
            }
        })
    }

    private fun showDialog(context: Context, view: ChipGroup, list: ArrayList<String>) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_tags_dialog)
        dialog.window?.setBackgroundDrawableResource(R.drawable.bg_white_rect)
        val width: Int =
            (context.getResources().getDisplayMetrics().widthPixels) - 140 //<-- int width=400;
        dialog.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        val skipBtn = dialog.findViewById(R.id.btn_skip) as Button
        val submitBtn = dialog.findViewById(R.id.btn_submit) as Button
        val codeTxt = dialog.findViewById<EditText>(R.id.code_txt)

        skipBtn.setOnClickListener {
            dialog.dismiss()
        }
        submitBtn.setOnClickListener {
            if (codeTxt.text.length > 0) {
                addNewChip(view, codeTxt.text.toString(), list)
            } else {
                showToast(getString(R.string.add_tag_error))
            }
            dialog.dismiss()
        }

        dialog.show()
    }
    fun openColorPicker() {
        colorPickerDialog.show()
    }
}