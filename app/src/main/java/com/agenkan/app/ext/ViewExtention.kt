package com.agenkan.app.ext

import android.app.Activity
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment


fun Activity.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}

fun Activity.showToast(stringId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, stringId, duration).show()
}

fun Fragment.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, text, duration).show()
}

fun Fragment.showToast(stringId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, stringId, duration).show()
}

fun View.visible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}