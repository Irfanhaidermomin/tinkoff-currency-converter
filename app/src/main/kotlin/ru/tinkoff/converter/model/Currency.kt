package ru.tinkoff.converter.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class Currency(
    @PrimaryKey
    @SerializedName("id")
    val id: String,

    @ColumnInfo(name = "name")
    @SerializedName("currencyName")
    val name: String,

    @ColumnInfo(name = "symbol")
    @SerializedName("currencySymbol")
    val symbol: String
) : Serializable {
    constructor() : this("", "", "")
}
