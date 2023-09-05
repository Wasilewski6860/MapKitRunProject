package com.example.mapkitproject.data.db

import com.example.mapkitproject.data.db.dto.RunDto

interface RunsStorage {


    suspend fun insertRun(runDto: RunDto)

    suspend fun deleteRun(runDto: RunDto)

    suspend fun getAllRunsSortedByDate(): List<RunDto>

    suspend fun getAllRunsSortedByTimeInMillis(): List<RunDto>

    suspend fun getAllRunsSortedByCaloriesBurned(): List<RunDto>

    suspend fun getAllRunsSortedByAvgSpeed(): List<RunDto>

    suspend fun getAllRunsSortedByDistance(): List<RunDto>

    suspend fun getTotalTimeInMillis(): Long

    suspend fun getTotalCaloriesBurned(): Int

    suspend fun getTotalDistance(): Int

    suspend fun getTotalAvgSpeed(): Float
}