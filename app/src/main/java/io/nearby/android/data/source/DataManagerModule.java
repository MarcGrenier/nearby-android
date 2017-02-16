package io.nearby.android.data.source;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

import dagger.Provides;
import io.nearby.android.data.source.local.SharedPreferencesHelper;
import io.nearby.android.data.source.local.SpottedLocalDataSource;
import io.nearby.android.data.source.remote.NearbyService;
import io.nearby.android.data.source.remote.ServiceCreator;
import io.nearby.android.data.source.remote.SpottedRemoteDataSource;

/**
 * Created by Marc on 2017-02-16.
 */
@Module
abstract class DataManagerModule {

    @Singleton
    @Binds
    @Local
    abstract SpottedDataSource provideSpottedLocalDataSource(SpottedLocalDataSource spottedLocalDataSource);

    @Singleton
    @Binds
    @Remote
    abstract SpottedDataSource provideSpottedRemoteDataSource(SpottedRemoteDataSource spottedRemoteDataSource);

    @Singleton
    @Binds
    @Local
    abstract SharedPreferencesHelper provideSharedPreferencesHelper(SharedPreferencesHelper sharedPreferencesHelper);

    @Singleton
    @Provides
    static NearbyService provideNearbyService(Context context, SharedPreferencesHelper sharedPreferencesHelper){
        ServiceCreator<NearbyService> creator = new ServiceCreator<>(NearbyService.class,
                NearbyService.ENDPOINT,
                context,
                sharedPreferencesHelper);
        return creator.create();
    }
}
