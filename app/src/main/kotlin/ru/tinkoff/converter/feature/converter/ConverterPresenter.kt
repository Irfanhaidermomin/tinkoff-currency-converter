package ru.tinkoff.converter.feature.converter

import com.arellomobile.mvp.InjectViewState
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.tinkoff.converter.model.Currency
import ru.tinkoff.converter.moxy.CoroutinesMvpPresenter
import ru.tinkoff.converter.storage.CurrenciesRepository
import java.math.BigDecimal

@InjectViewState
class ConverterPresenter : CoroutinesMvpPresenter<ConverterView>() {

    private val currenciesRepository: CurrenciesRepository = CurrenciesRepository(
        ConverterApp.convertingService,
        ConverterApp.database
    )
    private var currencies: List<Currency> = emptyList()
    private val errorHandler = CoroutineExceptionHandler { _, _ ->
        runBlocking(Main) {
            viewState.showError(currencies.isEmpty())
        }
    }
    private val asyncContext = IO + errorHandler

    override fun onFirstViewAttach() {
        loadCurrencies()
    }

    fun loadCurrencies() {
        coroutineContext.cancelChildren()
        launch(asyncContext) {
            val currencies = currenciesRepository.getCurrencies(this).await()
            withContext(Main) {
                this@ConverterPresenter.currencies = currencies
                viewState.showCurrencies(currencies)
                convert(0, 0)
            }
        }
    }

    fun convert(from: Int, to: Int, amount: BigDecimal = BigDecimal.valueOf(100)) {
        coroutineContext.cancelChildren()
        val currencyFrom: Currency = currencies.getOrNull(from) ?: return
        val currencyTo: Currency = currencies.getOrNull(to) ?: return
        launch(asyncContext) {
            val result = currenciesRepository.convert(currencyFrom, currencyTo, amount, this).await()
            withContext(Main) {
                viewState.showResult(currencyFrom, amount, currencyTo, result)
            }
        }
    }

    fun convertInverse(from: Int, to: Int, amount: BigDecimal = BigDecimal.ONE) {
        coroutineContext.cancelChildren()
        val currencyFrom: Currency = currencies.getOrNull(from) ?: return
        val currencyTo: Currency = currencies.getOrNull(to) ?: return
        launch(asyncContext) {
            val result = currenciesRepository.convert(currencyFrom, currencyTo, amount, this).await()
            withContext(Main) {
                viewState.showResult(currencyTo, result, currencyFrom, amount)
            }
        }
    }
}
