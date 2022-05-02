package com.logicsquare.parentsmeet.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE

import android.content.SharedPreferences
import com.logicsquare.parentsmeet.model.User

class SharedPref(context: Context) {
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("ParentsMeetPref", MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun loggedIn() {
        editor.putBoolean(LOGGED_IN, true).apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(LOGGED_IN, false)
    }

    fun logout() {
        editor.clear().apply()
    }

    fun saveUser(user: User?) {
        user?.let {
            loggedIn()
            editor.putString(FULL_NAME, user.name.full).apply()
            editor.putString(EMAIL, user.email).apply()
            editor.putString(PHONE, ""+user.phone).apply()
            editor.putString(PHONE_COUNTRY,""+user.phoneCountryCode).apply()
            editor.putString(USERTYPE, user.userType).apply()
            editor.putString(RELATION, user.relation).apply()
        }
    }



    fun saveFCMToken(token: String?) {
        editor.putString(FCM_TOKEN, token).apply()
    }

    fun getFCMToken(): String {
        return sharedPreferences.getString(FCM_TOKEN, "") ?: ""
    }

    fun saveToken(token: String?) {
        editor.putString(TOKEN, token).apply()
    }

    fun getToken(): String {
        return sharedPreferences.getString(TOKEN, "") ?: ""
    }

    fun getEmail(): String? {
        return sharedPreferences.getString(EMAIL, "")
    }
    fun getFullName(): String? {
        return sharedPreferences.getString(FULL_NAME, "")
    }

    fun getDob(): String {
        return sharedPreferences.getString(DOB, "")?: ""
    }

    fun getPhone(): String? {
        return sharedPreferences.getString(PHONE, "")
    }

    fun getPhoneCountry(): String? {
        return sharedPreferences.getString(PHONE_COUNTRY, "")
    }

    fun getUserType(): String? {
        return sharedPreferences.getString(USERTYPE, "")
    }

    fun saveKeepMe(checked: Boolean) {
        editor.putBoolean(KEEPME, checked).apply()
    }

    fun getKeepMe(): Boolean {
        return sharedPreferences.getBoolean(KEEPME, false)
    }

    companion object {
        const val LOGGED_IN = "LOGGED_IN"
        const val FULL_NAME = "fullName"
        const val EMAIL = "email"
        const val TOKEN = "token"
        const val FCM_TOKEN = "fcm_token"
        const val DOB = "dob"
        const val PHONE = "phone"
        const val PHONE_COUNTRY = "phoneCountry"
        const val USERTYPE = "userType"
        const val RELATION = "relation"
        const val KEEPME = "keepme"
    }
}