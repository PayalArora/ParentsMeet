package com.logicsquare.parentsmeet.ui.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.core.view.contains
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.logicsquare.parentsmeet.R
import com.logicsquare.parentsmeet.databinding.SettingsChildBinding
import com.logicsquare.parentsmeet.model.*
import com.logicsquare.parentsmeet.network.APIClient
import com.logicsquare.parentsmeet.network.APIInterface
import com.logicsquare.parentsmeet.ui.adapter.JobFilterAdapter
import com.logicsquare.parentsmeet.ui.adapter.OnItemClickEvent
import com.logicsquare.parentsmeet.ui.adapter.SpinnerAdapter
import com.logicsquare.parentsmeet.ui.adapter.onSpinnerItemClick
import com.logicsquare.parentsmeet.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChildPagerFragment(val kidsItem: KidsItem?,val kidsData: KidsData, val settingResponse: SettingResponse? ) : Fragment(),
    onSpinnerItemClick , OnItemClickEvent {
    private lateinit var mBinding: SettingsChildBinding
    var activitiesList = arrayListOf<String>()
    var activitiesMainList = arrayListOf<String>()
    var spinnerList = arrayListOf<String>()
    var gamesMainList = arrayListOf<String>()
    var gamesList = arrayListOf<String>()
    var needsList = arrayListOf<String>()
    var needsMainList = arrayListOf<String>()
    var timings = arrayListOf<String>()
    var  adapter:SpinnerAdapter? = null
    private lateinit var colorPickerDialog: ColorPickerDialog
    var colorBar = "#2F8390"
    var mypopupWindow: PopupWindow? = null

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
        mypopupWindow = setPopUpWindow()
        setListeners()
        mBinding.rvAvailability.apply {
            val gridLayoutManager = GridLayoutManager(requireContext(), 3)
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL)
            layoutManager = gridLayoutManager
            if (SettingsFragment.settingsResponse != null) {
                SettingsFragment.settingsResponse?.setting?.preferences?.timings.let {
                    adapter = JobFilterAdapter(1, it as List<Any?>, this@ChildPagerFragment)

                }
            }
        }
        kidsItem?.let {
            mBinding.etName.setText(it.name?.capitalizeWords())
            val ages = resources.getStringArray(R.array.Age)
            if (ages.contains(it.age)) {
                mBinding.spinnerAge.setSelection(ages.indexOf(it.age))
            }
            if (it.age.equals("Under 1"))
            {
                mBinding.spinnerAge.setSelection(0)
            }
            val grades = resources.getStringArray(R.array.Grades)
            if (grades.contains(it.grade)) {
                mBinding.spinnerGrade.setSelection(grades.indexOf(it.grade))
            }

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
            it.preferences?.timings?.let { it1 ->
                ((mBinding.rvAvailability.adapter) as JobFilterAdapter).update(it1)
            }
        }

        settingResponse?.setting?.preferences?.activities.let {
        activitiesMainList .addAll(it as ArrayList<String>)
            activitiesMainList.add("Other")
        }
        settingResponse?.setting?.preferences?.games.let {
        gamesMainList .addAll(it as ArrayList<String>)
            gamesMainList.add("Other")
        }
        settingResponse?.setting?.preferences?.specialNeeds.let {
        needsMainList .addAll(it as ArrayList<String>)
            needsMainList.add("Other")
        }

    }

    private fun setListeners() {
        mBinding.tagGames.setOnClickListener {
            //showDialog(requireContext(), mBinding.chipGames, gamesList)
            spinnerList.clear()
            spinnerList .addAll( gamesMainList)
            val location = IntArray(2)
            it.getLocationOnScreen(location)
            val p = Point()
            p.x = location[0]
            p.y = location[1]
            adapter?.setType(1)
            mypopupWindow?.showAsDropDown(it);

            adapter?.notifyDataSetChanged()
        }

        mBinding.tagActivities.setOnClickListener {
            spinnerList.clear()
            spinnerList .addAll( activitiesMainList)
            val location = IntArray(2)
            it.getLocationOnScreen(location)
            val p = Point()
            p.x = location[0]
            p.y = location[1]
            adapter?.setType(0)
            mypopupWindow?.showAsDropDown(it);
            adapter?.notifyDataSetChanged()

        }

        mBinding.tagNeeds.setOnClickListener {
           // showDialog(requireContext(), mBinding.chipNeeds, needsList)
            spinnerList.clear()
            spinnerList .addAll( needsMainList)
            val location = IntArray(2)
            it.getLocationOnScreen(location)
            val p = Point()
            p.x = location[0]
            p.y = location[1]
            adapter?.setType(2)
            mypopupWindow?.showAsDropDown(it);
            adapter?.notifyDataSetChanged()
        }
        mBinding.btnSave.setOnClickListener { updateKid() }
        mBinding.view.setOnClickListener {
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

        val grades = resources.getStringArray(R.array.Grades)
        val gradeAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_text_view, grades
        )
        mBinding.spinnerGrade.adapter = gradeAdapter

        val age = resources.getStringArray(R.array.Age)
        val ageAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_text_view, age
        )
        mBinding.spinnerAge.adapter = ageAdapter
    }

    fun addNewChip(view: ChipGroup, tag: String, list: ArrayList<String>):ArrayList<String> {
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
        if (tag.trim().length > 0 && (!list.contains(tag) && !list.contains(tag.toLowerCase()))) {
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
        return list
    }

    private fun updateKid() {
        val token = "Bearer ${SharedPref(requireContext()).getToken()}"
        var addKidRequest = UpdateKidRequest()
        var age = "0"
        if (mBinding.spinnerAge.selectedItem.toString().equals("Under 1"))
        {
            age = "0"
        } else
        {
            age =  mBinding.spinnerAge.selectedItem.toString()
        }
        addKidRequest.age = age
        addKidRequest.name = mBinding.etName.text.toString()
        addKidRequest.grade = mBinding.spinnerGrade.selectedItem.toString()
        addKidRequest.genderPronouns = mBinding.spinnerOther.selectedItem.toString().toLowerCase()
        addKidRequest.preferences.activities = activitiesList
        addKidRequest.preferences.timings = timings
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
                    if ( response.body()!=null) {
                        showToast(getString(R.string.saved))
                        kidsData.kidsData(response.body()?.kid)
                    }
                    hideProgressBar()
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
    fun setPopUpWindow(): PopupWindow {
        val inflater: LayoutInflater = layoutInflater
        requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        val view: View = inflater.inflate(R.layout.layout_recyclerview, null);
        adapter = SpinnerAdapter(spinnerList, this)
        view.findViewById<RecyclerView>(R.id.recycler_list).adapter = adapter

        val mypopupWindow = PopupWindow(view, 500, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        return mypopupWindow
    }

    override fun clickItem(position: Int, type:Int) {
        if (type == 0) {
            if (position == activitiesMainList.size - 1) {
                showDialog(requireContext(), mBinding.chipActivities, activitiesList)
            } else {
                activitiesList =  addNewChip(mBinding.chipActivities,
                    activitiesMainList.get(position),
                    activitiesList)
            }
        } else if (type == 1){
            if (position == gamesMainList.size - 1) {
                showDialog(requireContext(), mBinding.chipGames, gamesList)
            } else {
              gamesList =  addNewChip(mBinding.chipGames,
                    gamesMainList.get(position),
                    gamesList)
            }
        }else if (type == 2){
            if (position == needsMainList.size - 1) {
                showDialog(requireContext(), mBinding.chipNeeds, needsList)
            } else {
               needsList= addNewChip(mBinding.chipNeeds,
                    needsMainList.get(position),
                    needsList)
            }
        }
        mypopupWindow?.dismiss()
    }

    override fun onItemClick(position: ArrayList<String>, type: Int) {
        if (type == 1) {
            timings.clear()
            timings.addAll(position)
        }
    }
}


interface KidsData{
    fun kidsData(kid: Kid?);
}