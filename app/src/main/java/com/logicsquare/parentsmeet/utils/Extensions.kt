package com.logicsquare.parentsmeet.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import okhttp3.ResponseBody
import org.json.JSONObject
import java.lang.Exception
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