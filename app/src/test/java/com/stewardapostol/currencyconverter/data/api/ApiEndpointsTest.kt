package com.stewardapostol.currencyconverter.data.api

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ApiEndpointsTest {

    private val apiKey = "e953cf893944ab64a3c5474f"
    private val fromCurrency = "USD"
    private val toCurrency = "EUR"
    private val amount = 100.0

    @Test
    fun `test pairConversion generates correct URL`() {
        // Arrange
        val expectedUrl = "https://v6.exchangerate-api.com/v6/$apiKey/pair/$fromCurrency/$toCurrency/$amount"

        // Act
        val result = ApiEndpoints.pairConversion(apiKey, fromCurrency, toCurrency, amount)

        // Assert
        assertThat(result).isEqualTo(expectedUrl)
    }

    @Test
    fun `test supportedCodes generates correct URL`() {
        // Arrange
        val expectedUrl = "https://v6.exchangerate-api.com/v6/$apiKey/codes"

        // Act
        val result = ApiEndpoints.supportedCodes(apiKey)

        // Assert
        assertThat(result).isEqualTo(expectedUrl)
    }
}
