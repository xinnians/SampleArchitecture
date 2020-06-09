package com.example.repository.room

import android.content.Context
import androidx.room.*

@Database(entities = [Cart::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null
        private const val DATABASE_NAME = "local_db"

        fun getInstance(context: Context): LocalDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context.applicationContext).also {
                    INSTANCE = it
                }
            }

        private fun buildDatabase(appContext: Context): LocalDatabase  {
            return Room.databaseBuilder(appContext, LocalDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration() // Data is cache, so it is OK to delete
                .build()
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}