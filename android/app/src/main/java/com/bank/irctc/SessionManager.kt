package com.bank.irctc

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("IRCTC_PREFS", Context.MODE_PRIVATE)

    companion object {
        private const val USER_ID = "user_id"
        private const val USER_NAME = "user_name"
        private const val USER_EMAIL = "user_email"
        private const val IS_LOGGED_IN = "is_logged_in"
    }

    fun saveUser(id: Long, name: String, email: String) {
        prefs.edit().apply {
            putLong(USER_ID, id)
            putString(USER_NAME, name)
            putString(USER_EMAIL, email)
            putBoolean(IS_LOGGED_IN, true)
            apply()
        }
    }

    fun getUserId(): Long = prefs.getLong(USER_ID, -1)
    fun getUserName(): String? = prefs.getString(USER_NAME, "Passenger")
    fun isLoggedIn(): Boolean = prefs.getBoolean(IS_LOGGED_IN, false)

    fun logout() {
        prefs.edit().clear().apply()
    }
}
