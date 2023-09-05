package com.example.runappyt.di

import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.mapkitproject.MainActivity
import com.example.mapkitproject.R
import com.example.mapkitproject.domain.services.TrackingService
import com.example.mapkitproject.other.Constants

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val serviceModule = module {

    single<TrackingService> { TrackingService()  }

    factory<FusedLocationProviderClient>() { LocationServices.getFusedLocationProviderClient(
        androidContext() )}

//    factory { (activity: MainActivity) -> LocationServices.getFusedLocationProviderClient(activity) }

    factory<NotificationCompat.Builder> { NotificationCompat.Builder(androidContext(), Constants.NOTIFICATION_CHANNEL_ID)
        .setAutoCancel(false)
        .setOngoing(true)
        .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
        .setContentTitle("Running App")
        .setContentText("00:00:00")
        .setContentIntent(get()) }

    factory<PendingIntent> { PendingIntent.getActivity(
        androidContext(),
        0,
        Intent(androidContext(), MainActivity::class.java).also {
            it.action = Constants.ACTION_SHOW_TRACKING_FRAGMENT
        },
        PendingIntent.FLAG_UPDATE_CURRENT
    ) }
}