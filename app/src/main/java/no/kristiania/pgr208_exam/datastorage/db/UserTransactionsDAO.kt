package no.kristiania.pgr208_exam.datastorage.db

import no.kristiania.pgr208_exam.datastorage.entities.UserTransaction

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface UserTransactionsDAO {

    @Insert
    suspend fun insert(portfolio: UserTransaction)

    // Add function to get all users transactions

}