package com.agenkan.customer.data.model

data class User(
    var id: String = "",
    var name: String = "",
    var token: String = "",
    var isBlocked: Boolean = true
)