package io.nearby.android.ui.launcher;

import io.nearby.android.data.manager.AuthenticationManager;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class LauncherPresenter implements LauncherContract.Presenter {

    private LauncherContract.View view;

    public LauncherPresenter(LauncherContract.View view) {
        this.view = view;
    }


    @Override
    public void isUserLoggedIn() {
        AuthenticationManager.isUserLoggedIn()
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext(loginResult -> {
            switch (loginResult.getStatus()){
                case LOGGED_IN:
                    view.onUserLoggedIn();
                    break;
                case NOT_LOGGED_IN:
                    view.onUserLoggedIn();
            }
        }).subscribe();
    }
}
