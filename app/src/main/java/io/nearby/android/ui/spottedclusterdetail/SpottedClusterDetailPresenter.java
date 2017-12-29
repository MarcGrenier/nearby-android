package io.nearby.android.ui.spottedclusterdetail;

import io.nearby.android.data.manager.SpottedManager;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class SpottedClusterDetailPresenter implements SpottedClusterDetailContract.Presenter {

    private SpottedClusterDetailContract.View mView;

    public SpottedClusterDetailPresenter(SpottedClusterDetailContract.View view) {
        mView = view;
    }

    @Override
    public void getSpottedsDetails(double minLat, double maxLat, double minLng, double maxLng) {
        SpottedManager.getSpotteds(minLat, maxLat, minLng, maxLng, false)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(mView::onSpottedsReceived)
                .doOnComplete(() -> mView.hideProgressBar())
                .doOnError(throwable -> mView.spottedLoadingError())
                .subscribe();
    }
}
