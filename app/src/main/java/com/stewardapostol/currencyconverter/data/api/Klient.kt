package com.stewardapostol.currencyconverter.data.api

import com.stewardapostol.currencyconverter.data.model.ConversionResultEntity
import com.stewardapostol.currencyconverter.data.model.SupportedCodesResponse
import io.ktor.client.HttpClient

/**
 * The [Klient] interface defines the contract for interacting with the ExchangeRate API.
 * It provides methods for retrieving currency pair conversions and supported currency codes.
 */
interface Klient {

    /**
     * Creates and returns an instance of [HttpClient] configured to interact with the ExchangeRate API.
     *
     * @return A configured [HttpClient] instance used for making HTTP requests.
     */
    fun client(): HttpClient

    /**
     * Retrieves the currency conversion result for a given amount between two currencies.
     * This function makes a request to the ExchangeRate API to get the conversion rate.
     *
     * @param apiKey The API key required for authentication with the API.
     * @param fromCurrency The source currency code (e.g., "USD").
     * @param toCurrency The target currency code (e.g., "EUR").
     * @param amount The amount of the source currency to be converted.
     * @return A [ConversionResultEntity] containing the conversion result, or null if an error occurs.
     */
    suspend fun getPairConversion(
        apiKey: String,
        fromCurrency: String,
        toCurrency: String,
        amount: Double
    ): ConversionResultEntity?

    /**
     * Retrieves the supported currency codes from the ExchangeRate API.
     * This function makes a request to get all the currencies that are supported for conversion.
     *
     * @param apiKey The API key required for authentication with the API.
     * @return A [SupportedCodesResponse] containing the list of supported currency codes, or null if an error occurs.
     */
    suspend fun getSupportedCodes(apiKey: String): SupportedCodesResponse?
}
