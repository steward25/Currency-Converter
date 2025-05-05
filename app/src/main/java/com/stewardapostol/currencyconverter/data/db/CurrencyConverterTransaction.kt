package com.stewardapostol.currencyconverter.data.db

import com.stewardapostol.currencyconverter.data.model.AppsData
import com.stewardapostol.currencyconverter.data.model.ConversionResultEntity
import com.stewardapostol.currencyconverter.data.model.SupportedCodesResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Returns a Flow from Room DB based on the type of currency data requested.
 * This function dynamically fetches from the correct DAO.
 *
 * @param db The Room database instance
 * @param baseCode The base currency code (optional)
 * @return Flow of the requested data type or null if not matched
 */
inline fun <reified DATA : AppsData> getDAO(db: CurrencyConverterDatabase, baseCode: String? = null): Flow<DATA?> = flow {
    when (DATA::class) {
        SupportedCodesResponse::class -> {
            val data = db.currencyCodeDao().getAllCodes()
            emit(SupportedCodesResponse(supportedCodes = data) as DATA?)
        }
        ConversionResultEntity::class -> {
            val data = db.conversionResultDao().getLatestConversion()
            emit(data as DATA?)
        }
        else -> emit(null)
    }
}

/**
 * Inserts the given data into the appropriate DAO table.
 *
 * @param db The Room DB instance
 * @param data The data to insert
 */
suspend inline fun <reified DATA : AppsData> insertDao(db: CurrencyConverterDatabase, data: AppsData) {
    when (DATA::class) {
        SupportedCodesResponse::class -> (data as SupportedCodesResponse).supportedCodes?.let {
            db.currencyCodeDao().insertAll(it)
        }
        ConversionResultEntity::class -> db.conversionResultDao().insert(data as ConversionResultEntity)
        else -> error("Unsupported data type: ${DATA::class}")
    }
}

/**
 * Clears data from the appropriate table based on the type given.
 *
 * @param db The Room DB instance
 */
suspend inline fun <reified DATA : AppsData> deleteDao(db: CurrencyConverterDatabase) {
    when (DATA::class) {
        SupportedCodesResponse::class -> db.currencyCodeDao().clearAll()
        ConversionResultEntity::class -> db.conversionResultDao().clearAll()
        else -> error("Unsupported data type: ${DATA::class}")
    }
}
