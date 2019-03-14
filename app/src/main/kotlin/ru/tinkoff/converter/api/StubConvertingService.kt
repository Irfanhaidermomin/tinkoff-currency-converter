package ru.tinkoff.converter.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import ru.tinkoff.converter.model.Currency
import ru.tinkoff.converter.model.CurrencyRate
import ru.tinkoff.converter.model.ServerResponse
import java.math.BigDecimal
import kotlin.coroutines.CoroutineContext

@Deprecated("Replace with real one")
object StubConvertingService : ConvertingService, CoroutineScope {

    override fun getCurrencies(): Deferred<ServerResponse<Currency>> {
        return async {
            val results: Map<String, Currency> = linkedMapOf(
                    "RUB" to Currency("RUB", "Russian Ruble", ""),
                    "USD" to Currency("USD", "United States Dollar", ""),
                    "EUR" to Currency("EUR", "Euro", "")
            )
            ServerResponse(results)
        }
    }

    override fun getRate(query: String): Deferred<ServerResponse<CurrencyRate>> {
        return async {
            val (from, to) = query.split("_", limit = 2)
            val results: Map<String, CurrencyRate> = mapOf(
                    query to CurrencyRate(query, BigDecimal.ONE, from, to)
            )
            ServerResponse(results)
        }
    }

    override val coroutineContext: CoroutineContext = Dispatchers.IO
}