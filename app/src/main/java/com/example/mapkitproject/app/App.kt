package com.example.mapkitproject.app

import android.app.Application
import com.example.mapkitproject.R
import com.example.mapkitproject.di.sharedPrefsModule
import com.example.runappyt.di.dataModule
import com.example.runappyt.di.domainModule
import com.example.runappyt.di.presentationModule
import com.example.runappyt.di.serviceModule
import com.yandex.mapkit.MapKitFactory
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(R.string.yandex_maps_key.toString())
        MapKitFactory.initialize(this)
        startKoin {
            androidContext(this@App)
            androidLogger(Level.DEBUG)
            modules(listOf(dataModule, domainModule, presentationModule, serviceModule,sharedPrefsModule))
        }
    }
}