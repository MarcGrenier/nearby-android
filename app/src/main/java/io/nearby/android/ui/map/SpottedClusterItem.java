package io.nearby.android.ui.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import io.nearby.android.model.Spotted;

/**
 * Created by Marc on 2017-01-29.
 */

public class SpottedClusterItem extends Spotted implements ClusterItem {

    public SpottedClusterItem(Spotted spotted){
        super(spotted.getMessage(), spotted.getLatitude(), spotted.getLongitude());
    }

    @Override
    public LatLng getPosition() {
        return getLatLng();
    }
}