package io.nearby.android.ui.settings;

import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import io.nearby.android.data.User;
import io.nearby.android.data.source.DataManager;
import io.nearby.android.data.source.SpottedDataSource;
import io.nearby.android.ui.BasePresenter;

public class SettingsPresenter implements SettingsContract.Presenter {

    private final SettingsContract.View mView;

    public SettingsPresenter(SettingsContract.View view) {
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

    @Override
    public void linkFacebookAccount(LoginResult loginResult) {
        String userId = loginResult.getAccessToken().getUserId();
        String token = loginResult.getAccessToken().getToken();

        DataManager.getInstance().linkFacebookAccount(userId, token, new SpottedDataSource.FacebookLinkAccountCallback() {
            @Override
            public void onFacebookAccountAlreadyExist(String userId, String token) {
                mView.onFacebookAccountAlreadyExist(userId, token);
            }

            @Override
            public void onSuccess() {
                mView.onFacebookAccountLinked();
            }

            @Override
            public void onError(SpottedDataSource.ErrorType errorType) {
                if(!BasePresenter.manageError(mView, errorType)){
                    mView.linkAccountFailed();
                }
            }
        });
    }

    @Override
    public void linkGoogleAccount(GoogleSignInAccount account) {
        String userId = account.getId();
        String token = account.getIdToken();

        DataManager.getInstance().linkGoogleAccount(userId, token, new SpottedDataSource.GoogleLinkAccountCallback() {
            @Override
            public void onGoogleAccountAlreadyExist(String userId, String token) {
                mView.onGoogleAccountAlreadyExist(userId, token);
            }

            @Override
            public void onSuccess() {
                mView.onGoogleAccountLinked();
            }

            @Override
            public void onError(SpottedDataSource.ErrorType errorType) {
                if(!BasePresenter.manageError(mView, errorType)){
                    mView.linkAccountFailed();
                }
            }
        });
    }

    @Override
    public void logout() {
        DataManager.getInstance().signOut(new SpottedDataSource.Callback() {
            @Override
            public void onSuccess() {
                mView.onSignOutCompleted();
            }

            @Override
            public void onError(SpottedDataSource.ErrorType errorType) {
                if(!BasePresenter.manageError(mView, errorType)){
                    //Normally a sign out should never fail so we do as if we had a success.
                    mView.onSignOutCompleted();
                }
            }
        });
    }

    @Override
    public void deactivateAccount() {
        DataManager.getInstance().deactivateAccount(new SpottedDataSource.Callback() {
            @Override
            public void onSuccess() {
                mView.onAccountDeactivated();
            }

            @Override
            public void onError(SpottedDataSource.ErrorType errorType) {
                if(!BasePresenter.manageError(mView, errorType)){
                    mView.deactivateAccountFailed();
                }
            }
        });
    }

    @Override
    public void mergeFacebookAccount(String userId, String token) {
        DataManager.getInstance().mergeFacebookAccount(userId, token, new SpottedDataSource.Callback(){
            @Override
            public void onSuccess() {
                mView.onFacebookAccountMerged();
            }

            @Override
            public void onError(SpottedDataSource.ErrorType errorType) {
                if(!BasePresenter.manageError(mView, errorType)){
                    mView.mergeAccountFailed();
                }
            }
        });
    }

    @Override
    public void mergeGoogleAccount(String userId, String token) {
        DataManager.getInstance().mergeGoogleAccount(userId, token, new SpottedDataSource.Callback(){
            @Override
            public void onSuccess() {
                mView.onGoogleAccountMerged();
            }

            @Override
            public void onError(SpottedDataSource.ErrorType errorType) {
                if(!BasePresenter.manageError(mView, errorType)){
                    mView.mergeAccountFailed();
                }
            }
        });
    }
}
