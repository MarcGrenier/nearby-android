package io.nearby.android.ui.main;

import io.nearby.android.data.model.User;
import io.nearby.android.ui.BaseView;

public interface MainContract {

    interface View extends BaseView<Presenter>{

        void onUserInfoReceived(User user);
    }

    interface Presenter{
        void getUserInfo();
    }
}
