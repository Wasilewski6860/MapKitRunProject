package com.example.runappyt.di

import com.example.mapkitproject.MainViewModel
import com.example.mapkitproject.presentation.statistics_fragment.StatisticsViewModel

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {


    viewModel<MainViewModel>{
        MainViewModel(
            insertRunUseCase = get(),
            getAllRunsSortedByDatesUseCase = get(),
            getAllRunsSortedByDistanceUseCase = get(),
            getAllRunsSortedByCaloriesBurnedUseCase = get(),
            getAllRunsSortedByTimeInMillisUseCase = get(),
            getAllRunsSortedByAvgSpeedUseCase = get()
        )
    }
    viewModel<StatisticsViewModel>{
        StatisticsViewModel(
            getTotalTimeInMillisUseCase = get(),
            getTotalDistanceUseCase = get(),
            getTotalCaloriesBurnedUseCase = get(),
            getTotalAvgSpeedUseCase = get(),
            getAllRunsSortedByDatesUseCase = get()
        )
    }

//    viewModel<CatsViewModel> {
//        CatsViewModel(
//            getAllCatsUseCase = get(),
//            getCatsByNameUseCase = get(),
//            editCatUseCase = get()
//        )
//    }
//
//    viewModel<FavouritesViewModel> {
//        FavouritesViewModel(
//            getFavouriteCatsUseCase = get(),
//            getFavouriteCatsByNameUseCase = get(),
//            editCatUseCase = get()
//        )
//    }
//
//    viewModel<AddNewCatViewModel> {
//        AddNewCatViewModel(
//            getCatUseCase = get(),
//            editCatUseCase = get(),
//            addCatUseCase = get(),
//            getImageUseCase = get()
//        )
//    }
//
//
//    viewModel<DetailsViewModel> {
//        DetailsViewModel(deleteCatUseCase = get())
//    }
}