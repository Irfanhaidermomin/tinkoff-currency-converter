package ru.tinkoff.converter.api

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query
import ru.tinkoff.converter.model.Currency
import ru.tinkoff.converter.model.CurrencyRate
import ru.tinkoff.converter.model.ServerResponse

interface ConvertingService {
    fun getCurrencies(): Deferred<ServerResponse<Currency>>

    fun getRate(@Query("q") query: String): Deferred<ServerResponse<CurrencyRate>>
}
