package io.nearby.android.data.source;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;

import io.nearby.android.data.source.local.SharedPreferencesManager;
import io.nearby.android.data.source.remote.NearbyService;
import io.nearby.android.data.source.remote.ServiceCreator;
import io.nearby.android.util.GoogleApiClientBuilder;

/**
 * Created by Marc on 2017-02-16.
 */
abstract class DataManagerModule {


    static GoogleApiClient provideGoogleApiClient(Context context){
        return new GoogleApiClientBuilder(context)
                .addSignInApi()
                .build();
    }


    static NearbyService provideNearbyService(SharedPreferencesManager sharedPreferencesManager){
        ServiceCreator<NearbyService> creator = new ServiceCreator<>(NearbyService.class,
                NearbyService.ENDPOINT,
                sharedPreferencesManager);
        return creator.create();
    }
}
