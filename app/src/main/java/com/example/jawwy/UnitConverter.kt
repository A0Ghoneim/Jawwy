package com.example.jawwy

object UnitConverter {
    fun kelvinToCelsius(kelvin: Double): Double {
        return kelvin - 273.15
    }

    fun kelvinToFahrenheit(kelvin: Double): Double {
        return kelvin * 9/5 - 459.67
    }

    fun meterPerSecondToMilesPerHour(mps: Double): Double {
        val mph = mps * 2.23694
        return "%.2f".format(mph).toDouble()
    }
}