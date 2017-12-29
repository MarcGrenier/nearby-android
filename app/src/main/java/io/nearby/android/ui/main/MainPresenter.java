package io.nearby.android.ui.main;

import io.nearby.android.data.manager.ClientManager;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View view;

    public MainPresenter(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void getUserInfo() {
        ClientManager.getClientInfo()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(user -> view.onUserInfoReceived(user))
                .subscribe();
    }

}
