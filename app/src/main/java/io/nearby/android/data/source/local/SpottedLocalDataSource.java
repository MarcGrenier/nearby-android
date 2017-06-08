package io.nearby.android.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.File;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.nearby.android.data.Spotted;
import io.nearby.android.data.source.Local;
import io.nearby.android.data.source.SpottedDataSource;

public class SpottedLocalDataSource implements SpottedDataSource {

    private final GoogleApiClient mGoogleApiClient;
    private SharedPreferencesManager mSharedPreferencesManager;

    private static SpottedLocalDataSource instance;

    public static SpottedLocalDataSource getInstance(){
        if(instance == null){
            instance = new SpottedLocalDataSource();
        }

        return instance;
    }


    private SpottedLocalDataSource(SharedPreferencesManager sharedPreferencesManager,
                                  GoogleApiClient googleApiClient) {
        mSharedPreferencesManager = sharedPreferencesManager;
        mGoogleApiClient = googleApiClient;
        mGoogleApiClient.connect();
    }

    @Override
    public void isUserLoggedIn(UserLoginStatusCallback callback) {
        if(mSharedPreferencesManager.hasUserAlreadySignedIn()) {
            int method = mSharedPreferencesManager.getLastSignInMethod();

            switch (method) {
                case SharedPreferencesManager.LAST_SIGN_IN_METHOD_FACEBOOK:
                    facebookAuthentification(callback);
                    break;
                case SharedPreferencesManager.LAST_SIGN_IN_METHOD_GOOGLE:
                    googleAuthentification(callback);
                    break;
            }
        }
        else {
            callback.userIsNotLoggedIn();
        }
    }

    /**
     * If an AccessToken exists and is not expired, the user is considered logged in.
     * The finish() method call will be called if the user id logged in.
     *
     * From Facebook developpers site :
     * Native mobile apps using Facebook's SDKs will get long-lived access tokens,
     * good for about 60 days. These tokens will be refreshed once per day when the person
     * using your app makes a request to Facebook's servers. If no requests are made,
     * the token will expire after about 60 days and the person will have to go through the
     * login flow again to get a new token.
     *
     * https://developers.facebook.com/docs/facebook-login/access-tokens/expiration-and-extension
     * Visited 22-01-2017
     */
    /**
     * Verifies that the user is connected and has a valid token.
     */
    private void facebookAuthentification(final UserLoginStatusCallback callback){
        if(AccessToken.getCurrentAccessToken() != null){
            if(!AccessToken.getCurrentAccessToken().isExpired()) {
                AccessToken.refreshCurrentAccessTokenAsync(new AccessToken.AccessTokenRefreshCallback() {
                    @Override
                    public void OnTokenRefreshed(AccessToken accessToken) {
                        mSharedPreferencesManager.setLastSignInMethod(SharedPreferencesManager.LAST_SIGN_IN_METHOD_FACEBOOK);
                        mSharedPreferencesManager.setFacebookToken(accessToken.getToken());
                        callback.userIsLoggedIn();
                    }

                    @Override
                    public void OnTokenRefreshFailed(FacebookException exception) {
                        callback.userIsNotLoggedIn();
                    }
                });
            }
        }
    }

    /**
     * To validate that the google account exist. We try a silent sign_in.
     * If it fails, the client is not logded in.
     */
    private void googleAuthentification(final UserLoginStatusCallback callback){
        OptionalPendingResult<GoogleSignInResult> resultOptionalPendingResult = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if(resultOptionalPendingResult.isDone()){
            handleGoogleResult(resultOptionalPendingResult.get(), callback);
        }
        else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            resultOptionalPendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleGoogleResult(googleSignInResult, callback);
                }
            });
        }
    }

    private void handleGoogleResult(GoogleSignInResult googleSignInResult, UserLoginStatusCallback callback){
        if(googleSignInResult != null && googleSignInResult.isSuccess()){
            mSharedPreferencesManager.setLastSignInMethod(SharedPreferencesManager.LAST_SIGN_IN_METHOD_GOOGLE);
            mSharedPreferencesManager.setGoogleToken(googleSignInResult.getSignInAccount().getIdToken());
            callback.userIsLoggedIn();
        }
        else {
            callback.userIsNotLoggedIn();
        }
    }

    @Override
    public void facebookLogin(String userId, String token, LoginCallback callback) {
        
    }

    @Override
    public void googleLogin(String userId, String token, LoginCallback callback) {

    }

    @Override
    public void createSpotted(@NonNull Spotted spotted, @Nullable File image, SpottedCreatedCallback callback) {

    }

    @Override
    public void loadMySpotted(MySpottedLoadedCallback callback) {

    }

    @Override
    public void loadMyOlderSpotted(int spottedCount, MySpottedLoadedCallback callback) {

    }

    @Override
    public void getMyNewerSpotteds(Date myOlderSpotted, MySpottedLoadedCallback callback) {

    }

    @Override
    public void loadSpotted(double minLat, double maxLat, double minLng, double maxLng, boolean locationOnly, SpottedLoadedCallback callback) {

    }

    @Override
    public void loadSpottedDetails(String spottedId, SpottedDetailsLoadedCallback callback) {

    }

    @Override
    public boolean getDefaultAnonymity() {
        return mSharedPreferencesManager.getDefaultAnonymity();
    }

    @Override
    public void setDefaultAnonymity(boolean anonymity) {
        mSharedPreferencesManager.setDefaultAnonymity(anonymity);
    }

    @Override
    public void getUserInfo(UserInfoLoadedCallback callback) {
        // Do nothing
    }

    @Override
    public void linkFacebookAccount(String userId, String token, FacebookLinkAccountCallback callback) {
        // Do nothing
    }

    @Override
    public void linkGoogleAccount(String userId, String token, GoogleLinkAccountCallback callback) {
        // Do nothing
    }

    @Override
    public void mergeFacebookAccount(String userId, String token, Callback callback) {
        // Do nothing
    }

    @Override
    public void mergeGoogleAccount(String userId, String token, Callback callback) {
        // Do nothing
    }

    @Override
    public void signOut(final Callback callback) {
        clearFacebookLoginPrefenrences();
        clearGoogleLoginPrefenrences();

        LoginManager.getInstance().logOut();

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    callback.onSuccess();
                } else {
                    callback.onError(ErrorType.Other);
                }
            }
        });

    }

    @Override
    public void deactivateAccount(final Callback callback) {
        clearFacebookLoginPrefenrences();
        clearGoogleLoginPrefenrences();

        LoginManager.getInstance().logOut();

        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                if(status.isSuccess()){
                    callback.onSuccess();
                }
                else {
                    callback.onError(ErrorType.Other);
                }
            }
        });
    }

    /**
     * This methods should only be used when a 401 or 410 error is received that confirms
     * the fact that the account is already deactivated or unauthorized.
     */
    public void forceSignOutOrForceDeactivate(){
        clearFacebookLoginPrefenrences();
        clearGoogleLoginPrefenrences();

        LoginManager.getInstance().logOut();
    }

    public void setFacebookAuthPrefs(String userId, String token){
        mSharedPreferencesManager.setLastSignInMethod(SharedPreferencesManager.LAST_SIGN_IN_METHOD_FACEBOOK);
        mSharedPreferencesManager.setFacebookUserId(userId);
        mSharedPreferencesManager.setFacebookToken(token);
    }

    public void setGoogleAuthPrefs(String userId, String token){
        mSharedPreferencesManager.setLastSignInMethod(SharedPreferencesManager.LAST_SIGN_IN_METHOD_GOOGLE);
        mSharedPreferencesManager.setGoogleUserId(userId);
        mSharedPreferencesManager.setGoogleToken(token);
    }

    public void clearFacebookLoginPrefenrences(){
        mSharedPreferencesManager.setLastSignInMethod(SharedPreferencesManager.LAST_SIGN_IN_METHOD_NONE);
        mSharedPreferencesManager.setFacebookToken(null);
        mSharedPreferencesManager.setFacebookUserId(null);
    }

    public void clearGoogleLoginPrefenrences(){
        mSharedPreferencesManager.setLastSignInMethod(SharedPreferencesManager.LAST_SIGN_IN_METHOD_NONE);
        mSharedPreferencesManager.setGoogleToken(null);
        mSharedPreferencesManager.setGoogleUserId(null);
    }
}
