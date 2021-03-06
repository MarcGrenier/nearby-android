package io.nearby.android.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.Date;
import java.util.List;

import io.nearby.android.data.Spotted;
import io.nearby.android.data.User;

public interface SpottedDataSource {

    enum ErrorType{

        // 400 - Bad request
        // 403 - Forbidden
        // 404 - Not Found
        // 405 - Not allowed
        // 500 - Internal error
        Other,

        // 401 - Unauthorized
        UnauthorizedUser,

        // 410 - Gone
        DisabledUser
    }

    interface ErrorCallback{
        void onError(ErrorType errorType);
    }

    interface Callback extends ErrorCallback{
        void onSuccess();
    }

    interface UserLoginStatusCallback extends ErrorCallback{
        void userIsLoggedIn();
        void userIsNotLoggedIn();
    }

    interface SpottedCreatedCallback extends ErrorCallback{
        void onSpottedCreated();
    }

    interface MySpottedLoadedCallback extends ErrorCallback{
        void onMySpottedLoaded(List<Spotted> mySpotted);
    }

    interface UserInfoLoadedCallback extends ErrorCallback{
        void onUserInfoLoaded(User user);
    }

    interface FacebookLinkAccountCallback extends Callback {
        void onFacebookAccountAlreadyExist(String userId, String token);
    }

    interface GoogleLinkAccountCallback extends Callback {
        void onGoogleAccountAlreadyExist(String userId, String token);
    }

    interface SpottedDetailsLoadedCallback extends ErrorCallback{
        void onSpottedDetailsLoaded(Spotted spotted);
    }

    interface SpottedLoadedCallback extends ErrorCallback{
        void onSpottedLoaded(List<Spotted> spotted);
    }

    interface LoginCallback extends ErrorCallback{
        void onAccountCreated();
        void onLoginSuccess();
    }

    void isUserLoggedIn(UserLoginStatusCallback callback);

    void facebookLogin(String userId, String token, LoginCallback callback);

    void googleLogin(String userId, String token, LoginCallback callback);


    void createSpotted(@NonNull Spotted spotted, @Nullable File image , SpottedCreatedCallback callback);

    void loadMySpotted(MySpottedLoadedCallback callback);

    void loadMyOlderSpotted(int spottedCount, MySpottedLoadedCallback callback);

    void getMyNewerSpotteds(Date myOlderSpotted, MySpottedLoadedCallback callback);

    void loadSpotted(double minLat,double maxLat,double minLng, double maxLng, boolean locationOnly, SpottedLoadedCallback callback);

    void loadSpottedDetails(String spottedId, SpottedDetailsLoadedCallback callback);

    /**
     * Gets the default anonymity or the the last anonymity setting used.
     * The default returned value is true.
     * @return true if the user is anonymous.
     */
    boolean getDefaultAnonymity();

    void setDefaultAnonymity(boolean anonymity);

    void getUserInfo(UserInfoLoadedCallback callback);

    void linkFacebookAccount(String userId, String token, FacebookLinkAccountCallback callback);

    void linkGoogleAccount(String userId, String token, GoogleLinkAccountCallback callback);

    void mergeFacebookAccount(String userId, String token, Callback callback);

    void mergeGoogleAccount(String userId, String token, Callback callback);

    void signOut(Callback callback);

    void deactivateAccount(Callback callback);
}
