package io.nearby.android.data.api;

import java.io.IOException;

import io.nearby.android.data.SharedPreferencesManager;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Marc on 2017-12-16
 */

public class OkHttpInstance {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String SERVICE_PROVIDER_HEADER = "Service-Provider";

    private OkHttpClient okHttpClient;

    private static OkHttpInstance instance;

    public static OkHttpInstance getInstance() {
        if(instance == null){
            instance = new OkHttpInstance();
        }

        return instance;
    }

    private OkHttpInstance(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        addAuthentificationInterceptor(builder);
        okHttpClient = builder.build();
    }

    private void addAuthentificationInterceptor(OkHttpClient.Builder builder) {
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Request.Builder requestBuilder = originalRequest.newBuilder();

                int lastSignInMethod = SharedPreferencesManager.getInstance().getLastSignInMethod();

                String userId = "";
                String token = "";
                String provider = "";

                switch(lastSignInMethod){
                    case SharedPreferencesManager.LAST_SIGN_IN_METHOD_FACEBOOK:
                        provider = "Facebook";
                        userId = SharedPreferencesManager.getInstance().getFacebookUserId();
                        token = SharedPreferencesManager.getInstance().getFacebookToken();
                        break;
                    case SharedPreferencesManager.LAST_SIGN_IN_METHOD_GOOGLE:
                        provider = "Google";
                        userId = SharedPreferencesManager.getInstance().getGoogleUserId();
                        token = SharedPreferencesManager.getInstance().getGoogleToken();
                        break;
                }

                String authToken = Credentials.basic(userId, token);

                requestBuilder.header(AUTHORIZATION_HEADER,authToken);
                requestBuilder.addHeader(SERVICE_PROVIDER_HEADER, provider);

                Request request = requestBuilder.build();

                return chain.proceed(request);
            }
        });
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
