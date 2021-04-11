package no.kristiania.pgr208_exam.db

import androidx.room.Dao
import androidx.room.Insert
import no.kristiania.pgr208_exam.entities.UserAccounting

@Dao
interface UserAccountingDAO {
     @Insert
     suspend fun insert(userAccounting: UserAccounting)
}