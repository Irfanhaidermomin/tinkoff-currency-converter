package ru.tinkoff.converter.feature.converter

import com.arellomobile.mvp.MvpView
import ru.tinkoff.converter.model.Currency
import java.math.BigDecimal

interface ConverterView : MvpView {

    fun showCurrencies(currencies: List<Currency>)

    fun showError(noCurrencies: Boolean)

    fun showResult(from: Currency, amountIn: BigDecimal, to: Currency, amountOut: BigDecimal)
}
