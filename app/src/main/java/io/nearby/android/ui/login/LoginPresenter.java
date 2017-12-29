package io.nearby.android.ui.login;

import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import io.nearby.android.data.manager.AuthenticationManager;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;

    public LoginPresenter(LoginContract.View loginView) {
        view = loginView;
    }

    @Override
    public void loginWithFacebook(LoginResult loginResult) {
        /*
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                // Application code
                Timber.d("Graph request completed");
                if(response.getError() == null){
                    try {
                        String id = object.getString("id");
                        String name = object.getString("name");
                        String email = object.getString("email");
                    } catch (JSONException e) {
                        Timber.e("Graph request failed");
                    }
                }
            }
        });


        Bundle param = new Bundle();
        param.putString("fields","id,name,email");
        request.setParameters(param);
        request.executeAsync();
        */

        String token = loginResult.getAccessToken().getToken();
        String userId = loginResult.getAccessToken().getUserId();

        AuthenticationManager.facebookLogin(userId, token)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(loginResult1 -> {
                    switch (loginResult1.getStatus()) {
                        case LOGGED_IN:
                        case ACCOUNT_CREATED:
                            view.onLoginSuccessful();
                            break;
                        case NOT_LOGGED_IN:
                            view.onLoginFailed();
                    }
                })
                .subscribe();
    }

    @Override
    public void loginWithGoogle(GoogleSignInAccount account) {
        String idToken = account.getIdToken();
        String userId = account.getId();

        AuthenticationManager.googleLogin(userId, idToken)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(loginResult1 -> {
                    switch (loginResult1.getStatus()) {
                        case LOGGED_IN:
                        case ACCOUNT_CREATED:
                            view.onLoginSuccessful();
                            break;
                        case NOT_LOGGED_IN:
                            view.onLoginFailed();
                    }
                })
                .subscribe();
    }
}