package com.agenkan.customer.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.agenkan.customer.data.model.User
import com.agenkan.customer.data.repository.UserRepository


class MainViewModel(private val db: UserRepository) : ViewModel() {

    private val myUserLiveData = db.getMyUser()

    fun getUser(): LiveData<User> {
        return myUserLiveData
    }

    fun changeUserStatus() {
        db.changeUserStatus(!getUser().value?.isBlocked!!)
    }

}