package com.stewardapostol.currencyconverter.data.repository

import android.app.Application
import android.util.Log
import androidx.room.withTransaction
import com.stewardapostol.currencyconverter.data.api.DefaultKlient
import com.stewardapostol.currencyconverter.data.db.CurrencyConverterDatabase
import com.stewardapostol.currencyconverter.data.db.deleteDao
import com.stewardapostol.currencyconverter.data.db.getDAO
import com.stewardapostol.currencyconverter.data.db.insertDao
import com.stewardapostol.currencyconverter.data.model.AppsData
import com.stewardapostol.currencyconverter.data.model.ConversionResultEntity
import com.stewardapostol.currencyconverter.data.model.SupportedCodesResponse
import com.stewardapostol.currencyconverter.data.util.Resource
import com.stewardapostol.currencyconverter.data.util.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * The [Repository] object is responsible for fetching currency conversion data and supported currency codes.
 * It interacts with both the local database and the network to provide up-to-date information,
 * and handles caching and background fetching of data.
 */
object Repository {
    const val TAG = "Repository"

    /**
     * Fetches currency data from the local database or from a remote API based on certain conditions.
     * This function handles both fetching and saving of conversion data and supported codes.
     *
     * @param app The [Application] context used to access the database.
     * @param shouldFetch A flag indicating whether the data should be fetched from the network (default is false).
     * @param fromCurrencyCode The source currency code (e.g., "USD"). Default is an empty string.
     * @param toCurrencyCode The target currency code (e.g., "EUR"). Default is an empty string.
     * @param amount The amount to be converted. Default is 0.0.
     * @param apiKey The API key used for authentication with the remote API.
     * @return A [Flow] emitting [Resource] objects, which contain the data (or error) from the network and database.
     */
    inline fun <reified DATA : AppsData> getCurrencyData(
        app: Application,
        shouldFetch: Boolean = false,
        fromCurrencyCode: String = "",
        toCurrencyCode: String = "",
        amount: Double = 0.0,
        apiKey: String = ""
    ): Flow<Resource<DATA?>> {

        val db = CurrencyConverterDatabase.db(app)

        return flow {

            val resource = networkBoundResource(
                shouldFetch = { shouldFetch },
                query = {
                    Log.e(TAG, "@getCurrencyData: "+DATA::class)
                    when (DATA::class) {
                        SupportedCodesResponse::class -> {
                            getDAO<DATA>(db)
                        }

                        ConversionResultEntity::class -> {
                            getDAO<DATA>(db)
                        }
                        else -> flow {}
                    }
                },
                fetch = {
                    when (DATA::class) {
                        SupportedCodesResponse::class -> {
                            DefaultKlient().getSupportedCodes(apiKey)
                        }

                        ConversionResultEntity::class -> {
                            DefaultKlient().getPairConversion(
                                apiKey,
                                fromCurrencyCode,
                                toCurrencyCode,
                                amount
                            )
                        }
                        else -> AppsData()
                    }
                },
                saveFetchResult = { data ->
                    data?.let {
                        db.withTransaction {
                            deleteDao<DATA>(db)
                            insertDao<DATA>(db, it)
                        }
                    }
                }
            )

            // Emit the collected resource (data or error)
            resource.collect {
                emit(it)
            }
        }
    }
}
