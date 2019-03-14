package ru.tinkoff.converter.storage

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import ru.tinkoff.converter.api.ConvertingService
import ru.tinkoff.converter.model.Currency
import ru.tinkoff.converter.model.CurrencyRate
import ru.tinkoff.converter.orm.CurrencyDatabase
import java.math.BigDecimal

class CurrenciesRepository(
    private val api: ConvertingService,
    private val database: CurrencyDatabase
) {

    fun getCurrencies(coroutineScope: CoroutineScope): Deferred<List<Currency>> {
        return coroutineScope.getData(this::getCurrenciesFromDb) {
            val currencies = api.getCurrencies().await().results.values.toMutableList()
            currencies.sortWith(Comparator { lhs, rhs ->
                return@Comparator lhs.id.compareTo(rhs.id)
            })
            persistCurrencies(currencies)
            currencies
        }
    }

    fun getRate(from: Currency, to: Currency, coroutineScope: CoroutineScope): Deferred<CurrencyRate> {
        return coroutineScope.getData({ getRateFromDb(from, to) }) {
            val rate = api.getRate(buildQuery(from, to)).await().results.values.first()
            persistRate(rate)
            rate
        }
    }

    fun convert(
        from: Currency,
        to: Currency,
        amount: BigDecimal,
        coroutineScope: CoroutineScope
    ): Deferred<BigDecimal> {
        return coroutineScope.async {
            val rate = getRate(from, to, coroutineScope).await()
            rate.value.multiply(amount)
        }
    }

    private fun <T> CoroutineScope.getData(
        fromDb: () -> T?,
        fromServer: suspend () -> T
    ): Deferred<T> {
        return async { fromDb() ?: fromServer() }
    }

    private fun getRateFromDb(from: Currency, to: Currency): CurrencyRate? {
        val rate = database.currencyRateDao().getCurrencyRate(from.id, to.id) ?: return null
        if (rate.from != from.id && rate.to != to.id) {
            return !rate
        }
        return rate
    }

    private fun getCurrenciesFromDb(): List<Currency>? = database.currencyDao().getAll().takeIf { !it.isEmpty() }

    private fun persistRate(rate: CurrencyRate) {
        database.currencyRateDao().insert(rate)
    }

    private fun persistCurrencies(currencies: List<Currency>) {
        database.currencyDao().run {
            deleteAll()
            insert(currencies)
        }
    }

    private fun buildQuery(from: Currency, to: Currency): String = "${from.id}_${to.id}"
}
