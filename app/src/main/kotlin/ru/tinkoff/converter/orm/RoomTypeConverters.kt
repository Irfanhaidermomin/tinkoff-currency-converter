package ru.tinkoff.converter.orm

import androidx.room.TypeConverter
import java.math.BigDecimal

class RoomTypeConverters {

    @TypeConverter
    fun toBigDecimal(value: String): BigDecimal {
        return BigDecimal(value)
    }

    @TypeConverter
    fun fromBigDecimal(value: BigDecimal): String {
        return value.toPlainString()
    }
}