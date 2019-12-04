package com.agenkan.customer.data.preferences

import android.content.Context
import android.content.SharedPreferences

class AgenKanPreferences(context: Context) {

    private val sharedPref: SharedPreferences?

    init {
        sharedPref = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)
    }

    fun putIsBlocked(isBlocked: Boolean?) {
        with(sharedPref?.edit()!!) {
            putBoolean(KEY_BLOCKED, isBlocked!!)
            commit()
        }
    }

    fun getIsBlocked(): Boolean? {
        return sharedPref?.getBoolean(KEY_BLOCKED, true)
    }

    companion object {
        private val PREFERENCE_KEY: String? = "ak_preferences"
        private val KEY_BLOCKED: String? = "KEY_BLOCKED"
    }

}