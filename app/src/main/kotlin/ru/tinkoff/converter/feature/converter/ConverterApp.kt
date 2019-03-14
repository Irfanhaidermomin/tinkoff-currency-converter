package ru.tinkoff.converter.feature.converter

import android.app.Application
import androidx.room.Room
import ru.tinkoff.converter.api.ConvertingService
import ru.tinkoff.converter.api.StubConvertingService
import ru.tinkoff.converter.orm.CurrencyDatabase

class ConverterApp : Application() {

    companion object {
        lateinit var convertingService: ConvertingService
        lateinit var database: CurrencyDatabase
    }

    override fun onCreate() {
        super.onCreate()
        initDatabase()
        initApi()
    }

    private fun initDatabase() {
        database = Room.databaseBuilder(this, CurrencyDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    private fun initApi() {
        convertingService = StubConvertingService
    }
}
