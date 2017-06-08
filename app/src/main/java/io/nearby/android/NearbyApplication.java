package io.nearby.android;

import android.app.Application;

import timber.log.Timber;

public class NearbyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }
        else {
            // TODO Plant release plant.
        }

        //Facebook is automatically done when the manifest contains
        // the facebook app-id in a meta-data tag.

       // TODO initialize the DataManager.
    }
}
