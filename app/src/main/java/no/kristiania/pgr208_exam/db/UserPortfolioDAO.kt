package no.kristiania.pgr208_exam.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import no.kristiania.pgr208_exam.entities.UserPortfolio

@Dao
interface UserPortfolioDAO {

     @Insert
     suspend fun insert(portfolio: UserPortfolio)

     @Query("SELECT * FROM user_portfolio")
     suspend fun fetchAll(): List<UserPortfolio>

}