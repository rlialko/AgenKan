package com.agenkan.customer.service

import com.agenkan.customer.data.repository.UserRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.core.KoinComponent
import org.koin.core.inject

class MyFirebaseMessagingService : FirebaseMessagingService(), KoinComponent {

    private val userRepository: UserRepository by inject()

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        newMessage(message)
    }

    private fun newMessage(message: RemoteMessage) {

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendTokenToServer(token)
    }

    private fun sendTokenToServer(token: String) {
       userRepository.changeUserToken(token)
    }
}