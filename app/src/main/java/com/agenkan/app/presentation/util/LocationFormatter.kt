package com.agenkan.app.presentation.util

class LocationFormatter {

    companion object {
        fun format(longitude: Double?, latitude: Double?): String {
            if (longitude == null || latitude == null) {
                return "N/A"
            }
            return "$longitude, $latitude"
        }
    }
}