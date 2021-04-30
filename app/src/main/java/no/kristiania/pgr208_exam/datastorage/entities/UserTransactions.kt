package no.kristiania.pgr208_exam.datastorage.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_transactions")
data class UserTransaction(
        @PrimaryKey
        @ColumnInfo(name = "symbol")
        val symbol: String,
        @ColumnInfo(name = "volume")
        val volume: String

        // Add time/date
)