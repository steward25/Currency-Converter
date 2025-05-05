package com.stewardapostol.currencyconverter.data.api
import androidx.room.*
import com.stewardapostol.currencyconverter.data.model.CurrencyCode

@Dao
interface CurrencyCodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(codes: List<CurrencyCode>)

    @Query("SELECT * FROM currency_codes ORDER BY code ASC")
    suspend fun getAllCodes(): List<CurrencyCode>

    @Query("DELETE FROM currency_codes")
    suspend fun clearAll()
}