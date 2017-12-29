package io.nearby.android.ui.map.cluster

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

import io.nearby.android.data.model.Spotted

/**
 * Created by Marc on 2017-01-29
 */

data class SpottedClusterItem(val spotted: Spotted) : ClusterItem, Parcelable {
    override fun getPosition(): LatLng {
        return spotted.location.getLatLng()
    }

    constructor(source: Parcel) : this(
            source.readTypedObject(Spotted.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(spotted, flags)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SpottedClusterItem> = object : Parcelable.Creator<SpottedClusterItem> {
            override fun createFromParcel(source: Parcel): SpottedClusterItem = SpottedClusterItem(source)
            override fun newArray(size: Int): Array<SpottedClusterItem?> = arrayOfNulls(size)
        }
    }
}
