package io.nearby.android.ui.spotteddetail;

import io.nearby.android.data.Spotted;
import io.nearby.android.data.source.DataManager;
import io.nearby.android.data.source.SpottedDataSource;
import io.nearby.android.ui.BasePresenter;

public class SpottedDetailPresenter implements SpottedDetailContract.Presenter {

    private SpottedDetailContract.View mView;

    public SpottedDetailPresenter(SpottedDetailContract.View view) {
        this.mView = view;
    }

    @Override
    public void loadSpottedDetails(String spottedId) {
        DataManager.getInstance()
                .loadSpottedDetails(spottedId, new SpottedDataSource.SpottedDetailsLoadedCallback() {
            @Override
            public void onSpottedDetailsLoaded(Spotted spotted) {
                //Don't hide the progress bar. It will be done when the pictures are loaded.
                mView.onSpottedDetailsReceived(spotted);
            }

            @Override
            public void onError(SpottedDataSource.ErrorType errorType) {
                if(!BasePresenter.manageError(mView, errorType)){
                    mView.hideProgressBar();
                    mView.spottedDetailsLoadingError();
                }
            }
        });
    }
}
