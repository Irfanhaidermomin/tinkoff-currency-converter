package ru.tinkoff.converter.orm

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.tinkoff.converter.model.CurrencyRate

@Dao
interface CurrencyRateDao {
    @Query("SELECT * FROM currencyrate WHERE `from`=:from AND `to`=:to OR `from`=:to AND `to`=:from")
    fun getCurrencyRate(from: String, to: String): CurrencyRate?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(rate: CurrencyRate)
}
