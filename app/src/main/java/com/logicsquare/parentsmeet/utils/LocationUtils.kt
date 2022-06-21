package com.logicsquare.parentsmeet.utils

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val theta = lon1 - lon2
    var dist = (sin(deg2rad(lat1))
            * sin(deg2rad(lat2))
            + (cos(deg2rad(lat1))
            * cos(deg2rad(lat2))
            * cos(deg2rad(theta))))
    dist = acos(dist)
    dist = rad2deg(dist)
    dist *= 60 * 1.1515
    return dist
}

private fun deg2rad(deg: Double): Double {
    return deg * Math.PI / 180.0
}

private fun rad2deg(rad: Double): Double {
    return rad * 180.0 / Math.PI
}

fun distanceBetween(point1: LatLng?, point2: LatLng?): Double? {
    return if (point1 == null || point2 == null) {
        null
    } else convertIntoKm(SphericalUtil.computeDistanceBetween(point1, point2))
}

fun convertIntoKm(distance:Double):Double{
   return convertIntoMiles(String.format("%.2f", distance / 1000).toDouble())
}

fun convertIntoMiles(km: Double): Double {

    return String.format("%.1f", (km / 1.609)).toDouble()
}
