package com.example.mapkitproject.data.db.impl

import com.example.mapkitproject.data.db.RunsDatabase
import com.example.mapkitproject.data.db.RunsStorage
import com.example.mapkitproject.data.db.dto.RunDto


class RunsStorageImpl(runsDatabase: RunsDatabase): RunsStorage {

    private val runDao = runsDatabase.runsDao()


    override suspend fun insertRun(runDto: RunDto) {
        runDao.insertRun(runDto)
    }

    override suspend fun deleteRun(runDto: RunDto) {
        runDao.deleteRun(runDto)
    }

    override suspend fun getAllRunsSortedByDate(): List<RunDto> = runDao.getAllRunsSortedByDate()

    override suspend fun getAllRunsSortedByTimeInMillis(): List<RunDto> = runDao.getAllRunsSortedByTimeInMillis()

    override suspend fun getAllRunsSortedByCaloriesBurned(): List<RunDto> = runDao.getAllRunsSortedByCaloriesBurned()

    override suspend fun getAllRunsSortedByAvgSpeed(): List<RunDto> = runDao.getAllRunsSortedByAvgSpeed()

    override suspend fun getAllRunsSortedByDistance(): List<RunDto> = runDao.getAllRunsSortedByDistance()

    override suspend fun getTotalTimeInMillis(): Long = runDao.getTotalTimeInMillis()

    override suspend fun getTotalCaloriesBurned(): Int = runDao.getTotalCaloriesBurned()

    override suspend fun getTotalDistance(): Int = runDao.getTotalDistance()

    override suspend fun getTotalAvgSpeed(): Float = runDao.getTotalAvgSpeed()
}