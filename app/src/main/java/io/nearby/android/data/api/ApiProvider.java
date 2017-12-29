package io.nearby.android.data.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Marc on 2017-12-16
 */

public class ApiProvider {

    private static ApiProvider instance;
    private NearbyApi nearbyApi;

    public static ApiProvider getInstance() {
        if(instance == null){
            instance = new ApiProvider();
        }

        return instance;
    }

    private ApiProvider() {}

    public void init(String baseUrl){
        Gson gson = new GsonBuilder().create();

        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(OkHttpInstance.getInstance().getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        nearbyApi = mRetrofit.create(NearbyApi.class);
    }

    public NearbyApi getNearbyApi() {
        return nearbyApi;
    }

    public void setMockNearbyApi(NearbyApi nearbyApi){
        this.nearbyApi = nearbyApi;
    }
}
