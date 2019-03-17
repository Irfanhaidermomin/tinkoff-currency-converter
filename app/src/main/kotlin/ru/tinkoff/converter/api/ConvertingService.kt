package ru.tinkoff.converter.api

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query
import ru.tinkoff.converter.BuildConfig
import ru.tinkoff.converter.model.Currency
import ru.tinkoff.converter.model.CurrencyRate
import ru.tinkoff.converter.model.ServerResponse

interface ConvertingService {
    @GET("v6/currencies?apiKey=${BuildConfig.API_KEY}")
    fun getCurrencies(): Deferred<ServerResponse<Currency>>

    @GET("v6/convert?apiKey=${BuildConfig.API_KEY}")
    fun getRate(@Query("q") query: String): Deferred<ServerResponse<CurrencyRate>>
}
