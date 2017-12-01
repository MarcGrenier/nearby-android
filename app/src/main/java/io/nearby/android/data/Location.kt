package io.nearby.android.data

import com.google.android.gms.maps.model.LatLng

/**
 * Created by Marc on 2017-11-30
 */
data class Location(var latitude: Double,
                     var longitude: Double){

    private var latLng = LatLng(latitude, longitude)

    fun getLatLng() : LatLng{
        return latLng
    }
}

