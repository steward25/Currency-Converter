package com.stewardapostol.currencyconverter.data.api

/**
 * Object containing API endpoints for interacting with the ExchangeRate API.
 * Provides functions to construct URLs for different API endpoints.
 */
object ApiEndpoints {

    // Base URL for the ExchangeRate API
    private const val BASE_URL_EXCHANGE = "https://v6.exchangerate-api.com/v6/"

    /**
     * Constructs the URL for converting a specific amount of currency from one currency to another.
     *
     * @param apiKey The API key to authenticate with the ExchangeRate API.
     * @param fromCurrency The currency code of the source currency (e.g., "USD").
     * @param toCurrency The currency code of the target currency (e.g., "EUR").
     * @param amount The amount of the source currency to convert.
     * @return A string representing the URL to request the currency pair conversion.
     */
    fun pairConversion(apiKey: String, fromCurrency: String, toCurrency: String, amount: Double): String =
        "${BASE_URL_EXCHANGE}${apiKey}/pair/$fromCurrency/$toCurrency/$amount"

    /**
     * Constructs the URL to get the list of supported currency codes from the ExchangeRate API.
     *
     * @param apiKey The API key to authenticate with the ExchangeRate API.
     * @return A string representing the URL to request the supported currency codes.
     */
    fun supportedCodes(apiKey: String): String =
        "${BASE_URL_EXCHANGE}${apiKey}/codes"
}
