package com.example.runappyt.di

import com.example.runappyt.domain.use_cases.DeleteRunUseCase
import com.example.runappyt.domain.use_cases.GetAllRunsSortedByAvgSpeedUseCase
import com.example.runappyt.domain.use_cases.GetAllRunsSortedByCaloriesBurnedUseCase
import com.example.runappyt.domain.use_cases.GetAllRunsSortedByDatesUseCase
import com.example.runappyt.domain.use_cases.GetAllRunsSortedByDistanceUseCase
import com.example.runappyt.domain.use_cases.GetAllRunsSortedByTimeInMillisUseCase
import com.example.runappyt.domain.use_cases.GetTotalAvgSpeedUseCase
import com.example.runappyt.domain.use_cases.GetTotalCaloriesBurnedUseCase
import com.example.runappyt.domain.use_cases.GetTotalDistanceUseCase
import com.example.runappyt.domain.use_cases.GetTotalTimeInMillisUseCase
import com.example.runappyt.domain.use_cases.InsertRunUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<InsertRunUseCase> { InsertRunUseCase(runsRepository = get()) }
    factory<GetAllRunsSortedByAvgSpeedUseCase> { GetAllRunsSortedByAvgSpeedUseCase(runsRepository = get()) }
    factory<GetAllRunsSortedByDatesUseCase> { GetAllRunsSortedByDatesUseCase(runsRepository = get()) }
    factory<GetAllRunsSortedByDistanceUseCase> { GetAllRunsSortedByDistanceUseCase(runsRepository = get()) }
    factory<GetAllRunsSortedByTimeInMillisUseCase> { GetAllRunsSortedByTimeInMillisUseCase(runsRepository = get()) }
    factory<GetAllRunsSortedByCaloriesBurnedUseCase> { GetAllRunsSortedByCaloriesBurnedUseCase(runsRepository = get()) }
    factory<GetTotalDistanceUseCase> { GetTotalDistanceUseCase(runsRepository = get()) }
    factory<GetTotalTimeInMillisUseCase> { GetTotalTimeInMillisUseCase(runsRepository = get()) }
    factory<GetTotalAvgSpeedUseCase> { GetTotalAvgSpeedUseCase(runsRepository = get()) }
    factory<GetTotalCaloriesBurnedUseCase> { GetTotalCaloriesBurnedUseCase(runsRepository = get()) }
    factory<DeleteRunUseCase> { DeleteRunUseCase(runsRepository = get()) }

}