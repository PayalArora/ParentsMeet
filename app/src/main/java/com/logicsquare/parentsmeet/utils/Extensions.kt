package com.logicsquare.parentsmeet.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.logicsquare.parentsmeet.R
import okhttp3.ResponseBody
import org.json.JSONObject
import java.lang.Exception
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

const val regex = ("^(?=.*[0-9])"
        + "(?=.*[a-z])(?=.*[A-Z])"
        + "(?=.*[@#$%^&+=])"
        + "(?=\\S+$).{8,20}$")

fun AppCompatActivity.showToast(msg: String?) {
    msg?.let {
        Toast.makeText(this, it, Toast.LENGTH_LONG).show()
    }
}

fun Fragment.showToast(msg: String?) {
    (activity as AppCompatActivity).showToast(msg)
}

fun isEmailIdValid(emailId: String): Boolean {
    val regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$"
    val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(emailId)
    return matcher.find()
}


fun handleErrorResponse(body: ResponseBody?, context: Context) {
    try {
        body?.let {
            val jsonObject = JSONObject(it.string())
            Toast.makeText(context, jsonObject.optString("reason"), Toast.LENGTH_LONG).show()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// -------- Progress Bar -----//
fun Fragment.hideProgressBar() {
    ProgressUtil.hideLoading()
}

fun Activity.hideProgressBar() {
    ProgressUtil.hideLoading()
}

fun Fragment.showProgressBar() {
    ProgressUtil.showLoading(requireActivity())
}

fun Activity.showProgressBar() {
    ProgressUtil.showLoading(this)
}

fun String.toUpperCas(): String =
    this.replaceFirstChar{
        it.uppercase()
    }


fun Context.getProgressDrawable(): CircularProgressDrawable {
    val circularProgressDrawable = CircularProgressDrawable(this)
    circularProgressDrawable.strokeWidth = 10f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.progressRotation = 0.8f
    circularProgressDrawable.setColorSchemeColors(ResourcesCompat.getColor(this.resources,
        R.color.green_1,
        null))
    return circularProgressDrawable
}
fun String.capitalizeWords(): String = split(" ").map { it.capitalize() }.joinToString(" ")

fun Context.timeConverter(dateStr:String):String{
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val date: Date = dateFormat.parse(dateStr)
   val formattedDate= SimpleDateFormat("dd MMM, yyyy").format(date)
    return formattedDate
}
fun checkDate(dateStr: String): Boolean {
    try {
        var formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = formatter.parse(dateStr)
        return true
    } catch (parse: ParseException) {
        return false
    }
}

fun validateDate(dateStr: String): Date? {
    return try {
        var formatter = SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault())
        val date = formatter.parse(dateStr)
        date
    } catch (parse: ParseException) {
        null
    }
}

const val ID="_ID"
const val TITLE="_TITLE"
