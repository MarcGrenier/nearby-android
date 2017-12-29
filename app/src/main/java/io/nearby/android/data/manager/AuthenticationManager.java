package io.nearby.android.data.manager;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import io.nearby.android.data.SharedPreferencesManager;
import io.nearby.android.data.api.ApiProvider;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import okhttp3.ResponseBody;

/**
 * Created by Marc on 2017-12-16
 */

public class AuthenticationManager {

    public static Observable<LoginResult> isUserLoggedIn() {
        if (SharedPreferencesManager.getInstance().hasUserAlreadySignedIn()) {
            int method = SharedPreferencesManager.getInstance().getLastSignInMethod();

            switch (method) {
                case SharedPreferencesManager.LAST_SIGN_IN_METHOD_FACEBOOK:
                    return facebookAuthentication();
                case SharedPreferencesManager.LAST_SIGN_IN_METHOD_GOOGLE:
                    return googleAuthentication();
                default:
                    return Observable.just(new LoginResult(LoginResult.Status.NOT_LOGGED_IN));
            }
        } else {
            return Observable.just(new LoginResult(LoginResult.Status.NOT_LOGGED_IN));
        }
    }


    public static Observable<LoginResult> facebookLogin(String userId, String token) {
        setFacebookAuthPrefs(userId, token);
        return login();
    }

    public static Observable<LoginResult> googleLogin(String userId, String token) {
        setGoogleAuthPrefs(userId, token);
        return login();
    }

    public static Observable<ResponseBody> deactivateAccount() {
        signOut();

        return ApiProvider.getInstance().getNearbyApi()
                .deactivateAccount()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread());
    }

    public static void signOut() {
        clearFacebookLoginPrefenrences();
        clearGoogleLoginPrefenrences();

//        LoginManager.getInstance().logOut();

//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
//            @Override
//            public void onResult(@NonNull Status status) {
//                if (status.isSuccess()) {
//                    //callback.onSuccess();
//                } else {
//                    //callback.onError(SpottedDataSource.ErrorType.Other);
//                }
//            }
//        });
    }

    public static Observable<ResponseBody> linkFacebookAccount(final String userId, final String token) {
        return ApiProvider.getInstance().getNearbyApi()
                .linkFacebookAccount(userId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread());
    }

    public static Observable<ResponseBody> linkGoogleAccount(final String userId, final String token) {
        return ApiProvider.getInstance().getNearbyApi().linkGoogleAccount(userId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread());
    }

    public static Observable<ResponseBody> mergeFacebookAccount(String userId, String token) {
        return ApiProvider.getInstance().getNearbyApi().mergeFacebookAccount(userId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread());
    }

    public static Observable<ResponseBody> mergeGoogleAccount(String userId, String token) {
        return ApiProvider.getInstance().getNearbyApi().mergeGoogleAccount(userId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread());
    }

    /**
     * This methods should only be used when a 401 or 410 error is received that confirms
     * the fact that the account is already deactivated or unauthorized.
     */
    public static void forceSignOutOrForceDeactivate() {
        clearFacebookLoginPrefenrences();
        clearGoogleLoginPrefenrences();

        LoginManager.getInstance().logOut();
    }

    public static void setFacebookAuthPrefs(String userId, String token) {
        SharedPreferencesManager.getInstance().setLastSignInMethod(SharedPreferencesManager.LAST_SIGN_IN_METHOD_FACEBOOK);
        SharedPreferencesManager.getInstance().setFacebookUserId(userId);
        SharedPreferencesManager.getInstance().setFacebookToken(token);
    }

    public static void setGoogleAuthPrefs(String userId, String token) {
        SharedPreferencesManager.getInstance().setLastSignInMethod(SharedPreferencesManager.LAST_SIGN_IN_METHOD_GOOGLE);
        SharedPreferencesManager.getInstance().setGoogleUserId(userId);
        SharedPreferencesManager.getInstance().setGoogleToken(token);
    }

    public static void clearFacebookLoginPrefenrences() {
        SharedPreferencesManager.getInstance().setLastSignInMethod(SharedPreferencesManager.LAST_SIGN_IN_METHOD_NONE);
        SharedPreferencesManager.getInstance().setFacebookToken(null);
        SharedPreferencesManager.getInstance().setFacebookUserId(null);
    }

    public static void clearGoogleLoginPrefenrences() {
        SharedPreferencesManager.getInstance().setLastSignInMethod(SharedPreferencesManager.LAST_SIGN_IN_METHOD_NONE);
        SharedPreferencesManager.getInstance().setGoogleToken(null);
        SharedPreferencesManager.getInstance().setGoogleUserId(null);
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
    private static Observable<LoginResult> facebookAuthentication() {
        PublishSubject<LoginResult> publishSubject = PublishSubject.create();
        publishSubject.onNext(new LoginResult(LoginResult.Status.LOGGED_IN));
        publishSubject.onComplete();
//        if (AccessToken.getCurrentAccessToken() != null) {
//            if (!AccessToken.getCurrentAccessToken().isExpired()) {
//                AccessToken.refreshCurrentAccessTokenAsync(new AccessToken.AccessTokenRefreshCallback() {
//                    @Override
//                    public void OnTokenRefreshed(AccessToken accessToken) {
//                        SharedPreferencesManager.getInstance().setLastSignInMethod(SharedPreferencesManager.LAST_SIGN_IN_METHOD_FACEBOOK);
//                        SharedPreferencesManager.getInstance().setFacebookToken(accessToken.getToken());
//                        publishSubject.onNext(new LoginResult(LoginResult.Status.LOGGED_IN));
//                        publishSubject.onComplete();
//                    }
//
//                    @Override
//                    public void OnTokenRefreshFailed(FacebookException exception) {
//                        publishSubject.onNext(new LoginResult(LoginResult.Status.NOT_LOGGED_IN));
//                        publishSubject.onComplete();
//                    }
//                });
//            }
//        }

        return publishSubject;
    }

    /**
     * To validate that the google account exist. We try a silent sign_in.
     * If it fails, the client is not logded in.
     */
    private static Observable<LoginResult> googleAuthentication() {
        PublishSubject<LoginResult> publishSubject = PublishSubject.create();
        publishSubject.onNext(new LoginResult(LoginResult.Status.LOGGED_IN));
        publishSubject.onComplete();
//        OptionalPendingResult<GoogleSignInResult> resultOptionalPendingResult = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//
//        if (resultOptionalPendingResult.isDone()) {
//            publishSubject.onNext(handleGoogleResult(resultOptionalPendingResult.get()));
//            publishSubject.onComplete();
//        } else {
//
//            // If the user has not previously signed in on this device or the sign-in has expired,
//            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
//            // single sign-on will occur in this branch.
//            resultOptionalPendingResult.setResultCallback(googleSignInResult -> {
//                publishSubject.onNext(handleGoogleResult(googleSignInResult));
//                publishSubject.onComplete();
//            });
//
//        }

        return publishSubject;
    }

    private LoginResult handleGoogleResult(GoogleSignInResult googleSignInResult) {
        if (googleSignInResult != null && googleSignInResult.isSuccess()) {
            SharedPreferencesManager.getInstance().setLastSignInMethod(SharedPreferencesManager.LAST_SIGN_IN_METHOD_GOOGLE);
            SharedPreferencesManager.getInstance().setGoogleToken(googleSignInResult.getSignInAccount().getIdToken());
            return new LoginResult(LoginResult.Status.LOGGED_IN);
        } else {
            return new LoginResult(LoginResult.Status.NOT_LOGGED_IN);
        }
    }

    private static Observable<LoginResult> login() {
        return ApiProvider.getInstance().getNearbyApi()
                .login()
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .flatMap(response -> {
                    LoginResult loginResult = new LoginResult(LoginResult.Status.NOT_LOGGED_IN);
                    switch (response.code()) {
                        case 200:
                            //Normal login
                            loginResult.setStatus(LoginResult.Status.LOGGED_IN);
                            break;
                        case 201:
                            // Account created
                            loginResult.setStatus(LoginResult.Status.ACCOUNT_CREATED);
                            break;
                        default:
                            //TODO handle error
                    }

                    return Observable.just(loginResult);
                });
    }
}
