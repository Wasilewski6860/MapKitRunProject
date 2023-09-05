package com.example.runappyt.di

import com.example.mapkitproject.data.db.RunsDatabase
import com.example.mapkitproject.data.db.RunsStorage
import com.example.mapkitproject.data.db.impl.RunsStorageImpl
import com.example.mapkitproject.data.repository.RunsRepositoryImpl
import com.example.mapkitproject.domain.repositories.RunsRepository

import org.koin.dsl.module


val dataModule = module {

    single<RunsStorage> { RunsStorageImpl(runsDatabase = get()) }


    single<RunsRepository> { RunsRepositoryImpl(runsStorage = get()) }



    single<RunsDatabase> { RunsDatabase.getDataBase(context = get()) }
}