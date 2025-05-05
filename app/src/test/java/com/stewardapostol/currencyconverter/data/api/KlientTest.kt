package com.stewardapostol.currencyconverter.data.api

import com.stewardapostol.currencyconverter.data.model.ConversionResultEntity
import com.stewardapostol.currencyconverter.data.model.CurrencyCode
import com.stewardapostol.currencyconverter.data.model.SupportedCodesResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class KlientTest {

    private val testKlient = object : Klient {
        override fun client() = throw NotImplementedError("Not needed for test")

        override suspend fun getPairConversion(
            apiKey: String,
            fromCurrency: String,
            toCurrency: String,
            amount: Double
        ): ConversionResultEntity? {
            return ConversionResultEntity(
                baseCode = fromCurrency,
                targetCode = toCurrency,
                conversionRate = 1.1,
                conversionResult = amount * 1.1,
                timeLastUpdateUnix = 1234567890
            )
        }

        override suspend fun getSupportedCodes(apiKey: String): SupportedCodesResponse? {
            return SupportedCodesResponse(
                supportedCodes = listOf(
                    CurrencyCode("USD", "United States Dollar"),
                    CurrencyCode("EUR", "Euro")
                )
            )
        }
    }

    @Test
    fun `test getPairConversion returns expected result`() = runTest {
        val result = testKlient.getPairConversion("dummy", "USD", "EUR", 100.0)
        assertEquals("USD", result?.baseCode)
        assertEquals("EUR", result?.targetCode)
        result?.conversionResult?.let { assertEquals(110.0, it, 0.01) }
    }

    @Test
    fun `test getSupportedCodes returns expected codes`() = runTest {
        val result = testKlient.getSupportedCodes("dummy")
        val codes = result?.supportedCodes?.map { it }
        if (codes != null) {
            assertEquals(CurrencyCode("USD","United States Dollar"), codes[0])
        }
    }
}