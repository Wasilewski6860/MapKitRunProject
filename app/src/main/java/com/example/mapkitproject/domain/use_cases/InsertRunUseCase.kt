package com.example.runappyt.domain.use_cases

import com.example.mapkitproject.domain.models.Run
import com.example.mapkitproject.domain.repositories.RunsRepository


class InsertRunUseCase(private val runsRepository: RunsRepository) {

    suspend fun execute(run: Run) = runsRepository.insertRun(run)
}