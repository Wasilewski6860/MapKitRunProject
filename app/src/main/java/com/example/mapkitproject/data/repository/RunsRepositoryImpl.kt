package com.example.mapkitproject.data.repository

import com.example.mapkitproject.data.db.RunsStorage
import com.example.mapkitproject.data.db.dto.RunDto
import com.example.mapkitproject.domain.models.Run
import com.example.mapkitproject.domain.repositories.RunsRepository

class RunsRepositoryImpl(private val runsStorage: RunsStorage) : RunsRepository {



    private fun mapToData(run: Run): RunDto {
        with(run) {

            return RunDto(
                id = id,
                img = img,
                timestamp = timestamp,
                avgSpeedInKMH = avgSpeedInKMH,
                distanceInMeters = distanceInMeters,
                timeInMillis = timeInMillis,
                caloriesBurned = caloriesBurned
            )
        }
    }

    private fun mapToDomain(runDto: RunDto): Run {
        with(runDto) {
            return Run(
                id = id,
                img = img,
                timestamp = timestamp,
                avgSpeedInKMH = avgSpeedInKMH,
                distanceInMeters = distanceInMeters,
                timeInMillis = timeInMillis,
                caloriesBurned = caloriesBurned
            )
        }
    }

    override suspend fun insertRun(run: Run) {
        val mappedRun = mapToData(run)
        runsStorage.insertRun(mappedRun)
    }

    override suspend fun deleteRun(run: Run) {
        val mappedRun = mapToData(run)
        runsStorage.deleteRun(mappedRun)
    }

    override suspend fun getAllRunsSortedByDate(): List<Run> {
        val resultFromData = runsStorage.getAllRunsSortedByDate()
        return resultFromData.map { runDto ->
            mapToDomain(runDto)
        }
    }

    override suspend fun getAllRunsSortedByTimeInMillis(): List<Run> {
        val resultFromData = runsStorage.getAllRunsSortedByTimeInMillis()
        return resultFromData.map { runDto ->
            mapToDomain(runDto)
        }
    }

    override suspend fun getAllRunsSortedByCaloriesBurned(): List<Run> {
        val resultFromData = runsStorage.getAllRunsSortedByCaloriesBurned()
        return resultFromData.map { runDto ->
            mapToDomain(runDto)
        }
    }

    override suspend fun getAllRunsSortedByAvgSpeed(): List<Run> {
        val resultFromData = runsStorage.getAllRunsSortedByAvgSpeed()
        return resultFromData.map { runDto ->
            mapToDomain(runDto)
        }
    }

    override suspend fun getAllRunsSortedByDistance(): List<Run> {
        val resultFromData = runsStorage.getAllRunsSortedByDistance()
        return resultFromData.map { runDto ->
            mapToDomain(runDto)
        }
    }

    override suspend fun getTotalTimeInMillis(): Long = runsStorage.getTotalTimeInMillis()

    override suspend fun getTotalCaloriesBurned(): Int = runsStorage.getTotalCaloriesBurned()

    override suspend fun getTotalDistance(): Int = runsStorage.getTotalDistance()

    override suspend fun getTotalAvgSpeed(): Float = runsStorage.getTotalAvgSpeed()

}