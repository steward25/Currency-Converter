package com.stewardapostol.currencyconverter.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.stewardapostol.currencyconverter.data.api.ConversionResultDao
import com.stewardapostol.currencyconverter.data.api.CurrencyCodeDao
import com.stewardapostol.currencyconverter.data.model.ConversionResultEntity
import com.stewardapostol.currencyconverter.data.model.CurrencyCode
import com.stewardapostol.currencyconverter.data.util.ConversionRatesConverter

@Database(
    entities = [ConversionResultEntity::class, CurrencyCode::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ConversionRatesConverter::class)
abstract class CurrencyConverterDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: CurrencyConverterDatabase? = null

        fun db(context: Context): CurrencyConverterDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, CurrencyConverterDatabase::class.java, "currency_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

    abstract fun conversionResultDao(): ConversionResultDao
    abstract fun currencyCodeDao(): CurrencyCodeDao
}