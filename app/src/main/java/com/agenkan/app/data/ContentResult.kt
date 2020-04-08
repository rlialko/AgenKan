package com.agenkan.app.data


sealed class ContentResult<out R> {

    data class Success<out T>(val data: T) : ContentResult<T>()

    data class Error<out T>(val exception: Exception, val data: T? = null) : ContentResult<T>()
}