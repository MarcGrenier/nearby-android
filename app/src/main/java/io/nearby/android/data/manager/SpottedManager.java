package io.nearby.android.data.manager;

import android.support.annotation.Nullable;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.nearby.android.data.api.ApiProvider;
import io.nearby.android.data.api.NearbyApi;
import io.nearby.android.data.model.Spotted;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by Marc on 2017-12-16
 */

public class SpottedManager {

    public static Observable<ResponseBody> createSpotted(Spotted spotted, @Nullable File image) {
        RequestBody latitude = RequestBody.create(MultipartBody.FORM, Double.toString(spotted.getLocation().getLatitude()));
        RequestBody longitude = RequestBody.create(MultipartBody.FORM, Double.toString(spotted.getLocation().getLongitude()));
        RequestBody message = RequestBody.create(MultipartBody.FORM, spotted.getMessage());
        RequestBody anonymity = RequestBody.create(MultipartBody.FORM, Boolean.toString(spotted.getAnonymity()));

        NearbyApi nearbyApi = ApiProvider.getInstance().getNearbyApi();
        Observable<ResponseBody> observable;

        if (image != null) {
            RequestBody requestImage = RequestBody.create(MediaType.parse("image/*"), image);
            MultipartBody.Part body = MultipartBody.Part.createFormData("picture", image.getName(), requestImage);

            observable = nearbyApi.createSpotted(anonymity,
                    latitude,
                    longitude,
                    message,
                    body);
        } else {
            observable = nearbyApi.createSpotted(anonymity,
                    latitude,
                    longitude,
                    message);
        }

        observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread());

        return observable;
    }

    public static Observable<List<Spotted>> getMySpotted() {
        return ApiProvider.getInstance().getNearbyApi()
                .getMySpotteds()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread());
    }

    public static Observable<List<Spotted>> getMyOlderSpotted(int spottedCount) {
        return ApiProvider.getInstance().getNearbyApi()
                .getMyOlderSpotteds(spottedCount)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread());
    }

    public static Observable<List<Spotted>> getMyNewerSpotteds(Date myOlderSpotted) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedOldestSpottedDate = dateFormat.format(myOlderSpotted);
        return ApiProvider.getInstance().getNearbyApi()
                .getMyNewerSpotteds(formattedOldestSpottedDate)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread());
    }

    public static Observable<List<Spotted>> getSpotteds(double minLat, double maxLat, double minLng, double maxLng, boolean locationOnly) {
        // Add small buffer to min and max so that if we have multiple spotteds at the same
        // location, we won't send identical min and max.
        minLat -= 0.000001;
        minLng -= 0.000001;
        maxLat += 0.000001;
        maxLng += 0.000001;

        return ApiProvider.getInstance().getNearbyApi()
                .getSpotteds(minLat, maxLat, minLng, maxLng, locationOnly)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread());
    }

    public static Observable<Spotted> getSpottedDetails(String spottedId) {
        return ApiProvider.getInstance().getNearbyApi()
                .getSpotted(spottedId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread());
    }
}
