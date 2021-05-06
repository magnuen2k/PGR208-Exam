package no.kristiania.pgr208_exam.datastorage.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import no.kristiania.pgr208_exam.datastorage.entities.UserPortfolio

@Dao
interface UserPortfolioDAO {

     @Insert
     suspend fun insert(portfolio: UserPortfolio)

     @Query("SELECT * FROM user_portfolio")
     suspend fun fetchAll(): List<UserPortfolio>

     @Query("SELECT * FROM user_portfolio WHERE symbol =:symbol")
     suspend fun fetch(symbol : String) : UserPortfolio

     @Query("SELECT * FROM user_portfolio WHERE symbol = 'USD'")
     suspend fun fetchUsd() : UserPortfolio

     @Query("REPLACE INTO user_portfolio VALUES(:symbol, :volume)")
     suspend fun insertPortfolio(symbol : String, volume: String)

}