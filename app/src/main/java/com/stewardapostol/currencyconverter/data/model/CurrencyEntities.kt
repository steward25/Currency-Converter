package com.stewardapostol.currencyconverter.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stewardapostol.currencyconverter.data.util.CurrencyCodeListSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Response object for supported currency codes
@Serializable
data class SupportedCodesResponse(
    val result: String? = null, // API call result status
    val documentation: String? = null, // Link to API documentation
    @SerialName("terms_of_use")
    val termsOfUse: String? = null, // Terms of use URL
    @SerialName("supported_codes")
    @Serializable(with = CurrencyCodeListSerializer::class)
    val supportedCodes: List<CurrencyCode>? = null // List of supported currency codes
): AppsData()

// Represents a currency code and its full name
@Serializable
@Entity(tableName = "currency_codes")
data class CurrencyCode(
    @PrimaryKey
    @SerialName("code")
    val code: String, // e.g., "USD", "EUR"

    @SerialName("name")
    val name: String? = null  // Full currency name, e.g., "US Dollar"
): AppsData()

// Entity for a single currency conversion result
@Serializable
@Entity(tableName = "conversion_result")
data class ConversionResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Auto-generated primary key for Room

    val result: String? = null, // API result status
    val documentation: String? = null, // API documentation URL

    @SerialName("terms_of_use")
    val termsOfUse: String? = null, // API terms of use URL

    @SerialName("time_last_update_unix")
    val timeLastUpdateUnix: Long? = null, // Last update time (Unix timestamp)

    @SerialName("time_last_update_utc")
    val timeLastUpdateUtc: String? = null, // Last update time in UTC

    @SerialName("time_next_update_unix")
    val timeNextUpdateUnix: Long? = null, // Next update time (Unix timestamp)

    @SerialName("time_next_update_utc")
    val timeNextUpdateUtc: String? = null, // Next update time in UTC

    @SerialName("base_code")
    val baseCode: String? = null, // Base currency code (e.g., "AED")

    @SerialName("target_code")
    val targetCode: String? = null, // Target currency code (e.g., "USD")

    @SerialName("conversion_rate")
    val conversionRate: Double? = null, // Rate from base to target currency

    @SerialName("conversion_result")
    val conversionResult: Double? = null // Converted amount
): AppsData()

// Base class for app-level data models
@Serializable
open class AppsData
