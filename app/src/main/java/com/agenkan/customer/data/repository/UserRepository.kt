package com.agenkan.customer.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.agenkan.customer.data.model.User
import com.agenkan.customer.data.preferences.AgenKanPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class UserRepository(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val pref: AgenKanPreferences
) {

    private var myUserLiveData: MutableLiveData<User>? = null

    fun getMyUser(): LiveData<User> {
        return myUserLiveData ?: MutableLiveData<User>().also {
            myUserLiveData = it
            db.collection(DB_USERS)
                .document(TMP_ID)
                .addSnapshotListener { snapshot, _ ->
                    run {
                        val user = snapshot?.toObject(User::class.java)
                        myUserLiveData?.value = user
                        pref.putIsBlocked(user?.isBlocked)
                    }
                }
        }
    }

    fun changeUserStatus(blocked: Boolean) {
        db.collection(DB_USERS)
            .document(TMP_ID)
            .update(FIELD_BLOCKED, blocked)
    }

    fun changeUserToken(token: String) {
        db.collection(DB_USERS)
            .document(TMP_ID)
            .update(FIELD_TOKEN, token)
    }

    companion object {
        const val DB_USERS = "USERS"
        const val FIELD_TOKEN = "token"
        const val FIELD_BLOCKED = "blocked"
        const val TMP_ID = "VI0zpSkdCQO0vvTdxn1WqDTV5Gg1"
    }
}