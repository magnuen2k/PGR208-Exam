package no.kristiania.pgr208_exam.datastorage.db

import no.kristiania.pgr208_exam.datastorage.entities.UserTransaction

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserTransactionsDAO {

    @Insert
    suspend fun insert(portfolio: UserTransaction)

    @Query("SELECT * FROM user_transactions")
    suspend fun fetchAll() : List<UserTransaction>


}