package com.agenkan.customer.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.agenkan.customer.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class UserRepository(private val auth: FirebaseAuth, private val db: FirebaseFirestore) {

    private var myUserLiveData: MutableLiveData<User>? = null

    fun getMyUser(): LiveData<User> {
        return myUserLiveData ?: MutableLiveData<User>().also {
            myUserLiveData = it
            db.collection(DB_USERS)
                .document("VI0zpSkdCQO0vvTdxn1WqDTV5Gg1")
                .addSnapshotListener { snapshot, _ ->
                    myUserLiveData?.value = snapshot?.toObject(User::class.java)
                }
        }
    }

    companion object {
        const val DB_USERS = "USERS"
    }
}