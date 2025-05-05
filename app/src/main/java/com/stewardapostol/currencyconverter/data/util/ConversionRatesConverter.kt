package com.stewardapostol.currencyconverter.data.util

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

object ConversionRatesConverter {
    @TypeConverter
    fun fromMap(map: Map<String, Double>): String {
        return Json.encodeToString(map)
    }

    @TypeConverter
    fun toMap(json: String): Map<String, Double> {
        return Json.decodeFromString(json)
    }
}
