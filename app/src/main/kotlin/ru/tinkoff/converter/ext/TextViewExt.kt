package ru.tinkoff.converter.ext

import android.widget.TextView
import java.math.BigDecimal

fun TextView.textAsBigDecimal(defaultValue: BigDecimal = BigDecimal.ONE): BigDecimal {
    val text = text.toString().takeWhile { !it.isWhitespace() }
    return try {
        BigDecimal(text)
    } catch (ignored: NumberFormatException) {
        defaultValue
    }
}