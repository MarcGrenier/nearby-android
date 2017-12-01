package io.nearby.android.ui.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.nearby.android.ui.login.LoginActivity;
import io.nearby.android.ui.main.MainActivity;

/**
 * Created by Marc on 2017-01-22
 */

public class LauncherActivity extends AppCompatActivity implements LauncherContract.View{

    private LauncherPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new LauncherPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.isUserLoggedIn();
    }

    @Override
    public void onUserAccountDisabled() {
        onUserNotLoggedIn();
    }

    @Override
    public void onUserUnauthorized() {
        onUserNotLoggedIn();
    }

    @Override
    public void onUserNotLoggedIn() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onUserLoggedIn() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
