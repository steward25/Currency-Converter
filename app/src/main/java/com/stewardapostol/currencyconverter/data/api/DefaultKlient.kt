package com.stewardapostol.currencyconverter.data.api

import android.util.Log
import com.stewardapostol.currencyconverter.data.model.ConversionResultEntity
import com.stewardapostol.currencyconverter.data.model.SupportedCodesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * DefaultKlient is the implementation of the Klient interface that interacts with the ExchangeRate API.
 * It provides methods for creating an HTTP client and making requests to fetch currency pair conversion
 * and supported currency codes.
 */
class DefaultKlient : Klient {

    /**
     * Creates and configures an HttpClient instance for making HTTP requests to the ExchangeRate API.
     * The client is configured to use JSON serialization and includes response handling for errors.
     *
     * @return An instance of [HttpClient] that can be used to make HTTP requests.
     */
    override fun client(): HttpClient {
        return HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    useAlternativeNames = true
                    ignoreUnknownKeys = true
                    encodeDefaults = false
                })
            }

            install(ResponseObserver) {
                onResponse { response ->
                    // Log any non-2xx HTTP responses as errors
                    if (response.status.value in 300..599) {
                        throw ResponseException(response, "HTTP ${response.status.value}")
                    }
                    Log.e("Klient", "Response: ${response.status.value}")
                }
            }

            install(DefaultRequest) {
                // Set the default Content-Type header for requests
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }
    }

    /**
     * Retrieves the currency conversion result for a given amount between two currencies.
     *
     * @param apiKey The API key for authenticating the request.
     * @param fromCurrency The currency code for the source currency (e.g., "USD").
     * @param toCurrency The currency code for the target currency (e.g., "EUR").
     * @param amount The amount of the source currency to convert.
     * @return A [ConversionResultEntity] containing the conversion result, or null if an error occurs.
     */
    override suspend fun getPairConversion(
        apiKey: String,
        fromCurrency: String,
        toCurrency: String,
        amount: Double
    ): ConversionResultEntity? {
        return try {
            // Construct the URL for the currency pair conversion API endpoint
            val url = ApiEndpoints.pairConversion(apiKey, fromCurrency, toCurrency, amount)
            val response = client().get(url)
            // Return the parsed response body
            response.body()
        } catch (e: Exception) {
            Log.e("Klient", "Error in getPairConversion", e)
            null
        }
    }

    /**
     * Retrieves the supported currency codes from the ExchangeRate API.
     *
     * @param apiKey The API key for authenticating the request.
     * @return A [SupportedCodesResponse] containing the list of supported currency codes, or null if an error occurs.
     */
    override suspend fun getSupportedCodes(apiKey: String): SupportedCodesResponse? {
        return try {
            // Construct the URL for the supported currency codes API endpoint
            val url = ApiEndpoints.supportedCodes(apiKey)
            val response = client().get(url)
            // Return the parsed response body
            response.body()
        } catch (e: Exception) {
            Log.e("Klient", "Error in getSupportedCodes", e)
            null
        }
    }
}
