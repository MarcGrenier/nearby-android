package io.nearby.android.ui.main;

import io.nearby.android.data.User;
import io.nearby.android.data.source.DataManager;
import io.nearby.android.data.source.SpottedDataSource;
import io.nearby.android.ui.BasePresenter;

public class MainPresenter implements MainContract.Presenter{

    private MainContract.View mView;

    public MainPresenter(MainContract.View view){
        mView = view;
    }

    @Override
    public void getUserInfo() {
        DataManager.getInstance().getUserInfo(new SpottedDataSource.UserInfoLoadedCallback() {
            @Override
            public void onUserInfoLoaded(User user) {
                mView.onUserInfoReceived(user);
            }

            @Override
            public void onError(SpottedDataSource.ErrorType errorType) {
                if(!BasePresenter.manageError(mView, errorType)){
                    // Do nothing
                    // Keep both link account preferences disabled
                }
            }
        });
    }

}
