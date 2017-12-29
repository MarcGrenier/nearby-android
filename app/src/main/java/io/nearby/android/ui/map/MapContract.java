package io.nearby.android.ui.map;

import java.util.List;

import io.nearby.android.data.model.Spotted;
import io.nearby.android.ui.BaseView;

/**
 * Created by Marc on 2017-02-16
 */

public interface MapContract {

    interface View extends BaseView<Presenter>{
        void onSpottedsReceived(List<Spotted> spotteds);
    }

    interface Presenter{
        void getSpotteds(double minLat, double maxLat,
                         double minLng, double maxLng);
    }
}
