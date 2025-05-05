package com.stewardapostol.currencyconverter.data.api
import androidx.room.*
import com.stewardapostol.currencyconverter.data.model.ConversionResultEntity

@Dao
interface ConversionResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(conversion: ConversionResultEntity)

    @Query("SELECT * FROM conversion_result ORDER BY timeLastUpdateUnix DESC LIMIT 1")
    suspend fun getLatestConversion(): ConversionResultEntity?

    @Query("DELETE FROM conversion_result")
    suspend fun clearAll()
}