package ru.tinkoff.converter.orm

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.tinkoff.converter.model.Currency

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currency")
    fun getAll(): List<Currency>

    @Query("DELETE FROM currency")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(currencies: List<Currency>)
}
