package no.kristiania.pgr208_exam.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "user_accounting")
data class UserAccounting(
    @ColumnInfo(name = "symbol")
    val symbol: String,
    @ColumnInfo(name = "volume")
    val volume: String
)