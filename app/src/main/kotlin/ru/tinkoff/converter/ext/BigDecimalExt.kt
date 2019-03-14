package ru.tinkoff.converter.ext

import java.math.BigDecimal

fun BigDecimal.asString(): String {
    return setScale(2, BigDecimal.ROUND_HALF_DOWN).toPlainString()
}
