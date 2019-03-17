package ru.tinkoff.converter.feature.converter

import android.app.Application
import androidx.room.Room
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.tinkoff.converter.BuildConfig
import ru.tinkoff.converter.api.ConvertingService
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
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_ENDPOINT)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        convertingService = retrofit.create(ConvertingService::class.java)
    }
}
