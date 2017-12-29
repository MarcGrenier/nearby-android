package io.nearby.android.ui.myspotted;

import java.util.Date;

import io.nearby.android.data.manager.SpottedManager;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MySpottedPresenter implements MySpottedContract.Presenter {

    private MySpottedContract.View mView;

    public MySpottedPresenter(MySpottedContract.View view) {
        mView = view;
    }

    @Override
    public void loadMySpotted() {
        SpottedManager.getMySpotted()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(spotteds -> mView.onMySpottedReceived(spotteds))
                .doOnError(throwable -> mView.loadingMySpottedFailed())
                .doOnComplete(() -> mView.hideLoadingProgressBar())
                .subscribe();
    }

    @Override
    public void refreshMySpotted(Date myOlderSpotted) {
        SpottedManager.getMyNewerSpotteds(myOlderSpotted)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(spotteds -> mView.onMyNewerSpottedReceived(spotteds))
                .doOnError(throwable -> mView.refreshFailed())
                .doOnComplete(() -> mView.stopRefreshing())
                .subscribe();
    }

    @Override
    public void loadMyOlderSpotted(int spottedCount) {
        SpottedManager.getMyOlderSpotted(spottedCount)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(spotteds -> mView.onMyOlderSpottedReceived(spotteds))
                .doOnError(throwable -> mView.loadOlderFailed())
                .subscribe();
    }
}
