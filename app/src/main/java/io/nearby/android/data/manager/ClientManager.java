package io.nearby.android.data.manager;

import io.nearby.android.data.SharedPreferencesManager;
import io.nearby.android.data.api.ApiProvider;
import io.nearby.android.data.model.User;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Marc on 2017-12-16
 */

public class ClientManager {

    public static Observable<User> getClientInfo() {
        return ApiProvider.getInstance().getNearbyApi()
                .getUser()
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io());
    }


    public static boolean getDefaultAnonymity() {
        return SharedPreferencesManager.getInstance().getDefaultAnonymity();
    }


    public static void setDefaultAnonymity(boolean anonymity) {
        SharedPreferencesManager.getInstance().setDefaultAnonymity(anonymity);
    }
}
