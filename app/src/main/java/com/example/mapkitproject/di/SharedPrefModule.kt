package com.example.mapkitproject.di

import android.app.Application
import android.content.SharedPreferences
import com.example.mapkitproject.other.Constants.SHARED_PREFERENCES_NAME
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val sharedPrefsModule = module {

    single{
        getSharedPrefs(androidApplication())
    }

    single<SharedPreferences.Editor> {
        getSharedPrefs(androidApplication()).edit()
    }
}

fun getSharedPrefs(androidApplication: Application): SharedPreferences{
    return  androidApplication.getSharedPreferences(SHARED_PREFERENCES_NAME,  android.content.Context.MODE_PRIVATE)
}