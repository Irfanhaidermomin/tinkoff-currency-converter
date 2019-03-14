package ru.tinkoff.converter.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.math.BigDecimal
import java.math.RoundingMode

@Entity
data class CurrencyRate(

    @PrimaryKey
    @SerializedName("id")
    var id: String,

    @ColumnInfo(name = "value")
    @SerializedName("val")
    var value: BigDecimal = BigDecimal.ONE,

    @ColumnInfo(name = "from")
    @SerializedName("fr")
    var from: String,

    @ColumnInfo(name = "to")
    @SerializedName("to")
    var to: String
) : Serializable {
    constructor() : this("", BigDecimal.ONE, "", "")

    operator fun not(): CurrencyRate {
        val newValue = BigDecimal.ONE.divide(value, 2, RoundingMode.HALF_UP)
        return CurrencyRate("${to}_$from", newValue, to, from)
    }
}
