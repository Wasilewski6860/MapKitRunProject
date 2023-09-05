package com.example.runappyt.domain.use_cases

import com.example.mapkitproject.domain.models.Run
import com.example.mapkitproject.domain.repositories.RunsRepository

class GetAllRunsSortedByDistanceUseCase(private val runsRepository: RunsRepository) {

    suspend fun execute() = runsRepository.getAllRunsSortedByDistance()
}