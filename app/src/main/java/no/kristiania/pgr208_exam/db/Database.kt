package no.kristiania.pgr208_exam.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import no.kristiania.pgr208_exam.entities.UserPortfolio


const val DATABASE_NAME: String = "accounting_database"

@Database(entities = [UserPortfolio::class], version = 1)
abstract class DataBase : RoomDatabase() {
    abstract fun getUserPortfolioDAO() : UserPortfolioDAO
    companion object {
        private var db: DataBase? = null

        fun getDatabase(context: Context) : DataBase {
            val newDb = db ?: Room.databaseBuilder(context, DataBase::class.java, DATABASE_NAME).build()
            return newDb.also {
                db = it
            }
        }
    }
}