package com.example.mapkitproject.presentation.statistics_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapkitproject.domain.models.Run
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
import kotlinx.coroutines.launch

class StatisticsViewModel (
    private val getTotalTimeInMillisUseCase: GetTotalTimeInMillisUseCase,
    private val getTotalDistanceUseCase: GetTotalDistanceUseCase,
    private val getTotalCaloriesBurnedUseCase: GetTotalCaloriesBurnedUseCase,
    private val getTotalAvgSpeedUseCase: GetTotalAvgSpeedUseCase,
    private val getAllRunsSortedByDatesUseCase: GetAllRunsSortedByDatesUseCase
): ViewModel() {

    private val _totalTimeRun = MutableLiveData<Long>()
    val totalTimeRun: LiveData<Long> = _totalTimeRun

    private val _totalDistance = MutableLiveData<Int>()
    val totalDistance: LiveData<Int> = _totalDistance

    private val _totalCaloriesBurned = MutableLiveData<Int>()
    val totalCaloriesBurned: LiveData<Int> = _totalCaloriesBurned

    private val _totalAvgSpeed = MutableLiveData<Float>()
    val totalAvgSpeed: LiveData<Float> = _totalAvgSpeed

    private val _runsSortedByDate = MutableLiveData<List<Run>>()
    val runsSortedByDate: LiveData<List<Run>> = _runsSortedByDate



    fun fetchData() {
        viewModelScope.launch {
            try {
                _totalTimeRun.value = getTotalTimeInMillisUseCase.execute()
                _totalDistance.value = getTotalDistanceUseCase.execute()
                _totalCaloriesBurned.value = getTotalCaloriesBurnedUseCase.execute()
                _totalAvgSpeed.value = getTotalAvgSpeedUseCase.execute()
                _runsSortedByDate.value =getAllRunsSortedByDatesUseCase.execute()

            } catch (e: Exception) {
            }
        }
    }

}