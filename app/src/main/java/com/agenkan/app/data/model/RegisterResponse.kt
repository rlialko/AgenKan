package com.agenkan.app.data.model

data class RegisterResponse(
    var deviceData: DeviceResponse,
    val installId: String,
    val token: String
)