package com.example.mapkitproject.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mapkitproject.data.db.dto.RunDto

@Database(entities = [RunDto::class], version = 2, exportSchema = false)

abstract class RunsDatabase: RoomDatabase() {

    abstract fun runsDao(): RunsDao

    companion object {

        private var INSTANCE: RunsDatabase? = null

        fun getDataBase(context: Context): RunsDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    RunsDatabase::class.java,
                    "runs_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}