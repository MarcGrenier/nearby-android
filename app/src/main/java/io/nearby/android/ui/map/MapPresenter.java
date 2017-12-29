package io.nearby.android.ui.map;

import io.nearby.android.data.manager.SpottedManager;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MapPresenter implements MapContract.Presenter {

    private MapContract.View mView;

    public MapPresenter(MapContract.View mapView) {
        mView = mapView;
    }

    @Override
    public void getSpotteds(double minLat, double maxLat,
                            double minLng, double maxLng) {
        SpottedManager.getSpotteds(minLat, maxLat, minLng, maxLng, true)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(spotteds -> mView.onSpottedsReceived(spotteds))
                .subscribe();
    }
}
