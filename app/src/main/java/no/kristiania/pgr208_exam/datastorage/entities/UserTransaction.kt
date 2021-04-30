package no.kristiania.pgr208_exam.datastorage.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_transactions")
data class UserTransaction(
        @PrimaryKey
        @ColumnInfo(name = "time")
        val time: String,
        @ColumnInfo(name = "symbol")
        val symbol: String,
        @ColumnInfo(name = "volume")
        val volume: String,
        @ColumnInfo(name = "usd")
        val usdBuyAmount: String,
        @ColumnInfo(name = "type")
        val type: String
)