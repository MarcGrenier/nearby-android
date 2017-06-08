package io.nearby.android.ui.launcher;

import io.nearby.android.data.source.DataManager;
import io.nearby.android.data.source.SpottedDataSource;

public class LauncherPresenter implements LauncherContract.Presenter {

    private LauncherContract.View mView;

    public LauncherPresenter(LauncherContract.View view) {
        this.mView = view;
    }


    @Override
    public void isUserLoggedIn() {
        DataManager.getInstance().isUserLoggedIn(new SpottedDataSource.UserLoginStatusCallback() {
            @Override
            public void onError(SpottedDataSource.ErrorType errorType) {

            }

            @Override
            public void userIsLoggedIn() {
                mView.onUserLoggedIn();
            }

            @Override
            public void userIsNotLoggedIn() {
                mView.onUserNotLoggedIn();
            }
        });
    }
}
