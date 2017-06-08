package io.nearby.android.ui.map;

import java.util.List;

import io.nearby.android.data.Spotted;
import io.nearby.android.data.source.DataManager;
import io.nearby.android.data.source.SpottedDataSource;
import io.nearby.android.ui.BasePresenter;

public class MapPresenter implements MapContract.Presenter {

    private MapContract.View mView;

    public MapPresenter(MapContract.View mapView) {
        mView = mapView;
    }

    @Override
    public void getSpotteds(double minLat, double maxLat,
                            double minLng, double maxLng){
        DataManager.getInstance().loadSpotted(minLat, maxLat,
                minLng, maxLng,
                true,
                new SpottedDataSource.SpottedLoadedCallback() {
            @Override
            public void onSpottedLoaded(List<Spotted> spotted) {
                mView.onSpottedsReceived(spotted);
            }

            @Override
            public void onError(SpottedDataSource.ErrorType errorType) {
                if(!BasePresenter.manageError(mView, errorType)){
                    // Do nothing
                }
            }
        });
    }
}
