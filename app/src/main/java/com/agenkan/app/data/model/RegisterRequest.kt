package com.agenkan.app.data.model

data class RegisterRequest(
    var deviceId: String,
    var name: String,
    var imei: String,
    var fcmPushToken: String,
    var location: List<Double>
)