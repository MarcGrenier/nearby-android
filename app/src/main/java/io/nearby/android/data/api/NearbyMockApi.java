package io.nearby.android.data.api;

import java.util.ArrayList;
import java.util.List;

import io.nearby.android.data.model.Spotted;
import io.nearby.android.data.model.User;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by Marc on 2017-12-16
 */

public class NearbyMockApi implements NearbyApi {

    private List<Spotted> spotteds = new ArrayList<>();

    @Override
    public Observable<List<Spotted>> getMySpotteds() {
        List<Spotted> spotteds = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            spotteds.add(Spotted.DummyFactory.create());
        }
        return Observable.just(spotteds);
    }

    @Override
    public Observable<List<Spotted>> getMyOlderSpotteds(int skip) {
        List<Spotted> spotteds = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            spotteds.add(Spotted.DummyFactory.create());
        }
        return Observable.just(spotteds);
    }

    @Override
    public Observable<List<Spotted>> getMyNewerSpotteds(String since) {
        List<Spotted> spotteds = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            spotteds.add(Spotted.DummyFactory.create());
        }
        return Observable.just(spotteds);
    }

    @Override
    public Observable<List<Spotted>> getSpotteds(double minLat, double maxLat, double minLng, double maxLng, boolean locationOnly) {
        if(spotteds.isEmpty()){
            for (int i = 0; i < 1000; i++) {
                spotteds.add(Spotted.DummyFactory.create());
            }
        }
        return Observable.just(spotteds);
    }

    @Override
    public Observable<Spotted> getSpotted(String spottedId) {
        return Observable.just(Spotted.DummyFactory.create(Integer.parseInt(spottedId)));
    }

    @Override
    public Observable<User> getUser() {
        return Observable.just(User.DummyFactory.create());
    }

    @Override
    public Observable<Response<ResponseBody>> login() {
        return null;
    }

    @Override
    public Observable<ResponseBody> createSpotted(RequestBody anonymity, RequestBody lat, RequestBody lng, RequestBody message, MultipartBody.Part pictureFile) {
        ResponseBody responseBody = ResponseBody.create(MediaType.parse("UTF-8"), "");
        return Observable.just(responseBody);
    }

    @Override
    public Observable<ResponseBody> createSpotted(RequestBody anonymity, RequestBody lat, RequestBody lng, RequestBody message) {
        ResponseBody responseBody = ResponseBody.create(MediaType.parse("UTF-8"), "");
        return Observable.just(responseBody);
    }

    @Override
    public Observable<ResponseBody> linkFacebookAccount(String userId, String token) {
        return null;
    }

    @Override
    public Observable<ResponseBody> linkGoogleAccount(String googleId, String token) {
        return null;
    }

    @Override
    public Observable<ResponseBody> mergeFacebookAccount(String userId, String token) {
        return null;
    }

    @Override
    public Observable<ResponseBody> mergeGoogleAccount(String userId, String token) {
        return null;
    }

    @Override
    public Observable<ResponseBody> deactivateAccount() {
        return null;
    }
}
