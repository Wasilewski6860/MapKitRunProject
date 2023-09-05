package com.example.mapkitproject.domain.repositories

import com.example.mapkitproject.domain.models.Run

interface RunsRepository {

    suspend fun insertRun(run: Run)
    suspend fun deleteRun(run: Run)
    suspend fun getAllRunsSortedByDate(): List<Run>
    suspend fun getAllRunsSortedByTimeInMillis(): List<Run>
    suspend fun getAllRunsSortedByCaloriesBurned(): List<Run>
    suspend fun getAllRunsSortedByAvgSpeed(): List<Run>
    suspend fun getAllRunsSortedByDistance(): List<Run>
    suspend fun getTotalTimeInMillis(): Long
    suspend fun getTotalCaloriesBurned(): Int
    suspend fun getTotalDistance(): Int
    suspend fun getTotalAvgSpeed(): Float

}