package com.agenkan.app.presentation.util

class ImeiFormatter {

    companion object {
        fun format(imei: String?): String {
            if (imei.isNullOrEmpty()) {
                return "N/A"
            }
            return imei
        }
    }
}