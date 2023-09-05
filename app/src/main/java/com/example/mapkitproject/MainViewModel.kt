package com.example.mapkitproject

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapkitproject.domain.models.Run
import com.example.mapkitproject.other.SortType
import com.example.runappyt.domain.use_cases.GetAllRunsSortedByAvgSpeedUseCase
import com.example.runappyt.domain.use_cases.GetAllRunsSortedByCaloriesBurnedUseCase
import com.example.runappyt.domain.use_cases.GetAllRunsSortedByDatesUseCase
import com.example.runappyt.domain.use_cases.GetAllRunsSortedByDistanceUseCase
import com.example.runappyt.domain.use_cases.GetAllRunsSortedByTimeInMillisUseCase
import com.example.runappyt.domain.use_cases.InsertRunUseCase

import kotlinx.coroutines.launch

class MainViewModel (
    private val insertRunUseCase: InsertRunUseCase,
    private val getAllRunsSortedByDatesUseCase: GetAllRunsSortedByDatesUseCase,
    private val getAllRunsSortedByDistanceUseCase: GetAllRunsSortedByDistanceUseCase,
    private val getAllRunsSortedByCaloriesBurnedUseCase: GetAllRunsSortedByCaloriesBurnedUseCase,
    private val getAllRunsSortedByTimeInMillisUseCase: GetAllRunsSortedByTimeInMillisUseCase,
    private val getAllRunsSortedByAvgSpeedUseCase: GetAllRunsSortedByAvgSpeedUseCase
): ViewModel() {


    val runs = MediatorLiveData<List<Run>>()

    var sortType = SortType.DATE



    fun sortRuns(sortType: SortType) = when(sortType) {

        SortType.DATE ->{
            viewModelScope.launch {
                try {
                    runs.value = getAllRunsSortedByDatesUseCase.execute()
                } catch (e: Exception) {
                }
            }
        }
        SortType.RUNNING_TIME ->{
            viewModelScope.launch {
                try {
                    runs.value = getAllRunsSortedByTimeInMillisUseCase.execute()
                } catch (e: Exception) {
                }
            }
        }
        SortType.AVG_SPEED ->{
            viewModelScope.launch {
                try {
                    runs.value = getAllRunsSortedByAvgSpeedUseCase.execute()
                } catch (e: Exception) {
                }
            }
        }
        SortType.DISTANCE ->{
            viewModelScope.launch {
                try {
                    runs.value = getAllRunsSortedByDistanceUseCase.execute()
                } catch (e: Exception) {
                }
            }
        }
        SortType.CALORIES_BURNED ->{
            viewModelScope.launch {
                try {
                    runs.value = getAllRunsSortedByCaloriesBurnedUseCase.execute()
                } catch (e: Exception) {
                }
            }
        }
    }.also {
        this.sortType = sortType
    }

    fun insertRun(run: Run) = viewModelScope.launch {
        insertRunUseCase.execute(run)
    }
}