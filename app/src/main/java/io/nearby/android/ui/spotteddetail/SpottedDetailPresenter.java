package io.nearby.android.ui.spotteddetail;

import io.nearby.android.data.manager.SpottedManager;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class SpottedDetailPresenter implements SpottedDetailContract.Presenter {

    private SpottedDetailContract.View mView;

    public SpottedDetailPresenter(SpottedDetailContract.View view) {
        this.mView = view;
    }

    @Override
    public void loadSpottedDetails(String spottedId) {
        SpottedManager.getSpottedDetails(spottedId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(mView::onSpottedDetailsReceived)
                .doOnError(throwable -> mView.spottedDetailsLoadingError())
                .doOnComplete(() -> mView.hideProgressBar())
                .subscribe();
    }
}
