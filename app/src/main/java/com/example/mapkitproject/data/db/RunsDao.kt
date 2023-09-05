package com.example.mapkitproject.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mapkitproject.data.db.dto.RunDto

@Dao
interface  RunsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: RunDto)

    @Delete
    suspend fun deleteRun(run: RunDto)

    @Query("SELECT * FROM running_table ORDER BY timestamp DESC")
    suspend fun getAllRunsSortedByDate(): List<RunDto>

    @Query("SELECT * FROM running_table ORDER BY timeInMillis DESC")
    suspend fun getAllRunsSortedByTimeInMillis(): List<RunDto>

    @Query("SELECT * FROM running_table ORDER BY caloriesBurned DESC")
    suspend fun getAllRunsSortedByCaloriesBurned(): List<RunDto>

    @Query("SELECT * FROM running_table ORDER BY avgSpeedInKMH DESC")
    suspend fun getAllRunsSortedByAvgSpeed(): List<RunDto>

    @Query("SELECT * FROM running_table ORDER BY distanceInMeters DESC")
    suspend fun getAllRunsSortedByDistance(): List<RunDto>

    @Query("SELECT SUM(timeInMillis) FROM running_table")
    suspend fun getTotalTimeInMillis(): Long

    @Query("SELECT SUM(caloriesBurned) FROM running_table")
    suspend fun getTotalCaloriesBurned(): Int

    @Query("SELECT SUM(distanceInMeters) FROM running_table")
    suspend fun getTotalDistance(): Int

    @Query("SELECT AVG(avgSpeedInKMH) FROM running_table")
    suspend fun getTotalAvgSpeed(): Float

}