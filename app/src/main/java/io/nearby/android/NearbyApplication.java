package io.nearby.android;

import android.app.Application;

import io.nearby.android.data.SharedPreferencesManager;
import io.nearby.android.data.api.ApiProvider;
import io.nearby.android.data.api.NearbyApi;
import io.nearby.android.data.api.NearbyMockApi;
import timber.log.Timber;

public class NearbyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }
        else {
            Timber.plant(new Timber.Tree() {
                @Override
                protected void log(int priority, String tag, String message, Throwable t) {
                    // Don't log
                }
            });
        }

        //Facebook is automatically done when the manifest contains
        // the facebook app-id in a meta-data tag.

        SharedPreferencesManager.getInstance().init(getApplicationContext());

        ApiProvider.getInstance().init(NearbyApi.ENDPOINT);
        ApiProvider.getInstance().setMockNearbyApi(new NearbyMockApi());
    }
}
