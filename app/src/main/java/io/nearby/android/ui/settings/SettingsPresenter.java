package io.nearby.android.ui.settings;

import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import io.nearby.android.data.manager.AuthenticationManager;
import io.nearby.android.data.manager.ClientManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.HttpException;

public class SettingsPresenter implements SettingsContract.Presenter {

    private final SettingsContract.View mView;

    public SettingsPresenter(SettingsContract.View view) {
        mView = view;
    }

    @Override
    public void getUserInfo() {
        ClientManager.getClientInfo()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(mView::onUserInfoReceived)
                .subscribe();
    }

    @Override
    public void linkFacebookAccount(LoginResult loginResult) {
        String userId = loginResult.getAccessToken().getUserId();
        String token = loginResult.getAccessToken().getToken();

        AuthenticationManager.linkFacebookAccount(userId, token)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(responseBody -> mView.onFacebookAccountLinked())
                .doOnError(throwable -> {
                    boolean accountAlreadyExist = false;
                    if (throwable instanceof HttpException) {
                        HttpException exception = (HttpException) throwable;
                        if (exception.code() == 403) {
                            accountAlreadyExist = true;
                            mView.onFacebookAccountAlreadyExist(userId, token);
                        }
                    }

                    if (!accountAlreadyExist) {
                        mView.linkAccountFailed();
                    }
                })
                .subscribe();
    }

    @Override
    public void linkGoogleAccount(GoogleSignInAccount account) {
        String userId = account.getId();
        String token = account.getIdToken();

        AuthenticationManager.linkGoogleAccount(userId, token)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(responseBody -> mView.onGoogleAccountLinked())
                .doOnError(throwable -> {
                    boolean accountAlreadyExist = false;
                    if (throwable instanceof HttpException) {
                        HttpException exception = (HttpException) throwable;
                        if (exception.code() == 403) {
                            accountAlreadyExist = true;
                            mView.onGoogleAccountAlreadyExist(userId, token);
                        }
                    }

                    if (!accountAlreadyExist) {
                        mView.linkAccountFailed();
                    }
                })
                .subscribe();
    }

    @Override
    public void logout() {
        AuthenticationManager.signOut();
        mView.onSignOutCompleted();
    }

    @Override
    public void deactivateAccount() {
        AuthenticationManager.deactivateAccount()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(responseBody -> mView.onAccountDeactivated())
                .doOnError(throwable -> mView.deactivateAccountFailed())
                .subscribe();
    }

    @Override
    public void mergeFacebookAccount(String userId, String token) {
        AuthenticationManager.mergeFacebookAccount(userId, token)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(responseBody -> mView.onFacebookAccountMerged())
                .doOnError(throwable -> mView.mergeAccountFailed())
                .subscribe();
    }

    @Override
    public void mergeGoogleAccount(String userId, String token) {
        AuthenticationManager.mergeGoogleAccount(userId, token)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(responseBody -> mView.onGoogleAccountMerged())
                .doOnError(throwable -> mView.mergeAccountFailed())
                .subscribe();
    }
}
