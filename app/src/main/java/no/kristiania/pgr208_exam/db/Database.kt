package no.kristiania.pgr208_exam.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import no.kristiania.pgr208_exam.entities.UserAccounting

const val DATABASE_NAME: String = "user_accounting_database"

@Database(entities = [UserAccounting::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun getUserAccountingDAO() : UserAccountingDAO

    companion object {
        private var db: Database? = null

        // Get the singleton instance of Database object.
        fun getDatabase(context: Context): Database {
            val newDb =
                    db ?: Room.databaseBuilder(context, Database::class.java, DATABASE_NAME).build()
            return newDb.also {
                db = it
            }
        }

    }
}