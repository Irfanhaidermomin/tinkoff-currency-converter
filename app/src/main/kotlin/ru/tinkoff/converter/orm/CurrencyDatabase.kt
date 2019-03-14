package ru.tinkoff.converter.orm

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.tinkoff.converter.model.Currency
import ru.tinkoff.converter.model.CurrencyRate

@Database(entities = [Currency::class, CurrencyRate::class], version = 1)
@TypeConverters(RoomTypeConverters::class)
abstract class CurrencyDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao

    abstract fun currencyRateDao(): CurrencyRateDao
}
