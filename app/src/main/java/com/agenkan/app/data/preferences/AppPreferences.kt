package com.agenkan.app.data.preferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class AppPreferences(context: Context) {

    private val sharedPref: SharedPreferences?

    init {
        sharedPref = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)
    }

    fun putIsBlocked(isBlocked: Boolean?) {
        Log.d(TAG, "putIsBlocked() $isBlocked")
        with(sharedPref?.edit()!!) {
            putBoolean(KEY_BLOCKED, isBlocked!!)
            commit()
        }
    }

    fun getIsBlocked(): Boolean? {
        val blocked = sharedPref?.getBoolean(KEY_BLOCKED, false)
        Log.d(TAG, "getIsBlocked() $blocked")
        return blocked
    }

    fun getToken(): String {
        val token = sharedPref?.getString(KEY_TOKEN, "")!!
        Log.d(TAG, "getToken() $token")
        return token
    }

    fun putToken(token: String) {
        Log.d(TAG, "putToken() $token")
        with(sharedPref?.edit()!!) {
            putString(KEY_TOKEN, token)
            commit()
        }
    }

    fun getId(): Int {
        val id = sharedPref?.getInt(KEY_ID, 0)!!
        Log.d(TAG, "getId() $id")
        return id
    }

    fun putId(id: Int) {
        Log.d(TAG, "putId() $id")
        with(sharedPref?.edit()!!) {
            putInt(KEY_ID, id)
            commit()
        }
    }

    fun clear() {
        Log.d(TAG, "clear()")
        with(sharedPref?.edit()!!) {
            clear()
            commit()
        }
    }

    fun getHash(): String {
        val value = sharedPref?.getString(KEY_HASH, "")!!
        Log.d(TAG, "getHash() $value")
        return value
    }


    fun putHash(passwordHash: String?) {
        Log.d(TAG, "putHash() $passwordHash")
        with(sharedPref?.edit()!!) {
            putString(KEY_HASH, passwordHash)
            commit()
        }
    }

    companion object {
        private val PREFERENCE_KEY: String? = "ak_preferences"
        private val KEY_BLOCKED: String? = "KEY_BLOCKED"
        private val KEY_TOKEN: String? = "KEY_TOKEN"
        private val KEY_ID: String? = "KEY_ID"
        private val KEY_HASH: String? = "KEY_HASH"
        private val TAG: String? = "AppPreferences"
    }

}