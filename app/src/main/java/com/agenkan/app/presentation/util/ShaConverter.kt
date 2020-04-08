package com.agenkan.app.presentation.util

import java.security.MessageDigest
import java.util.*

class ShaConverter {

    companion object {
        private fun byteArrayToString(bytes: ByteArray): String? {
            val buffer = StringBuilder()
            for (b in bytes) {
                buffer.append(java.lang.String.format(Locale.getDefault(), "%02x", b))
            }
            return buffer.toString()
        }

        fun convert(clearString: String): String? {
            return try {
                val messageDigest =
                    MessageDigest.getInstance("SHA-1")
                messageDigest.update(clearString.toByteArray(charset("UTF-8")))
                byteArrayToString(messageDigest.digest())
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}