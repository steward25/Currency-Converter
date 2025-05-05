package com.stewardapostol.currencyconverter.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.stewardapostol.currencyconverter.data.model.ConversionResultEntity
import com.stewardapostol.currencyconverter.data.model.CurrencyCode
import com.stewardapostol.currencyconverter.data.model.SupportedCodesResponse
import com.stewardapostol.currencyconverter.data.repository.Repository
import com.stewardapostol.currencyconverter.data.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CurrencyConversionViewModel (application: Application) : AndroidViewModel(application) {

    // MutableStateFlow to hold the list of supported currencies.
    private val _supportedCurrencies = MutableStateFlow<Resource<List<CurrencyCode>?>>(Resource.Loading())
    // StateFlow to expose the supported currencies data.  This is read-only.
    val supportedCurrencies: StateFlow<Resource<List<CurrencyCode>?>> = _supportedCurrencies

    // MutableStateFlow to hold the result of a currency conversion.
    private val _conversionResult = MutableStateFlow<Resource<ConversionResultEntity?>>(Resource.Loading())
    // StateFlow to expose the conversion result.
    val conversionResult: StateFlow<Resource<ConversionResultEntity?>> = _conversionResult

    /**
     * Fetches the list of supported currencies.
     *
     * @param apiKey The API key to use for the request.
     * @param shouldFetch Whether to force a network fetch (true) or use cached data if available (false).
     */
    fun fetchSupportedCurrencies(apiKey: String, shouldFetch: Boolean = false) {
        viewModelScope.launch {
            Repository.getCurrencyData<SupportedCodesResponse>(
                app = getApplication(),
                shouldFetch = shouldFetch,
                fromCurrencyCode = "",
                toCurrencyCode = "",
                amount = 0.0,
                apiKey = apiKey
            ).collectLatest { resource ->
                _supportedCurrencies.value = Resource.Success(resource.data?.supportedCodes)
            }
        }
    }

    /**
     * Fetches the conversion result for a specific currency pair.
     *
     * @param fromCurrencyCode The code of the source currency.
     * @param toCurrencyCode The code of the target currency.
     * @param amount The amount to convert.
     * @param apiKey The API key.
     * @param shouldFetch Whether to force a network fetch.
     */
    fun fetchPairConversion(
        fromCurrencyCode: String,
        toCurrencyCode: String,
        amount: Double,
        apiKey: String,
        shouldFetch: Boolean = false
    ) {
        viewModelScope.launch {
            Repository.getCurrencyData<ConversionResultEntity>(
                app = getApplication(),
                fromCurrencyCode = fromCurrencyCode,
                toCurrencyCode = toCurrencyCode,
                amount = amount,
                shouldFetch = shouldFetch,
                apiKey = apiKey
            ).collectLatest { resource ->
                _conversionResult.value = Resource.Success(resource.data)
            }
        }
    }
}
