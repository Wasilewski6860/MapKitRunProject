package com.example.mapkitproject.domain.models

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Run(
    var id : Int =0,
    var img: String,
    var timestamp: Long,
    var avgSpeedInKMH: Float,
    var distanceInMeters: Int,
    var timeInMillis: Long,
    var caloriesBurned: Int
): Parcelable