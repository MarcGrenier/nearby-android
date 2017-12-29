package io.nearby.android.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.StringRes;

import io.nearby.android.R;


public class SharedPreferencesManager {

    public static final int LAST_SIGN_IN_METHOD_NONE = 0;
    public static final int LAST_SIGN_IN_METHOD_GOOGLE = 1;
    public static final int LAST_SIGN_IN_METHOD_FACEBOOK = 2;

    private SharedPreferences mPrefs;
    private Context context;

    private static SharedPreferencesManager instance;

    public static SharedPreferencesManager getInstance() {
        if (instance == null) {
            instance = new SharedPreferencesManager();
        }

        return instance;
    }

    private SharedPreferencesManager() {
    }

    public void init(Context context) {
        this.context = context;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean hasUserAlreadySignedIn() {
        boolean hasAlready = mPrefs.contains(context.getString(R.string.pref_last_social_login_used));

        if (hasAlready) {
            hasAlready = mPrefs.getInt(context.getString(R.string.pref_last_social_login_used), LAST_SIGN_IN_METHOD_NONE) != LAST_SIGN_IN_METHOD_NONE;
        } else {
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putInt(context.getString(R.string.pref_last_social_login_used), LAST_SIGN_IN_METHOD_NONE);
            editor.commit();
        }

        return hasAlready;
    }

    public int getLastSignInMethod() {
        int lastMethod = LAST_SIGN_IN_METHOD_NONE;

        if (mPrefs.contains(context.getString(R.string.pref_last_social_login_used))) {
            lastMethod = mPrefs.getInt(context.getString(R.string.pref_last_social_login_used), LAST_SIGN_IN_METHOD_NONE);
        }

        return lastMethod;
    }

    public void setLastSignInMethod(int method) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(context.getString(R.string.pref_last_social_login_used), method);
        editor.commit();
    }


    /*
     *  Token Management
     */

    private String getToken(@StringRes int prefKey, String defaultValue) {
        String token = "";

        if (mPrefs.contains(context.getString(prefKey))) {
            token = mPrefs.getString(context.getString(prefKey), defaultValue);
        }

        return token;
    }

    private void setToken(@StringRes int prefKey, String token) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(context.getString(prefKey), token);
        editor.commit();
    }

    private String getUserId(@StringRes int prefKey) {
        String userId = "";

        if (mPrefs.contains(context.getString(prefKey))) {
            userId = mPrefs.getString(context.getString(prefKey), "");
        }

        return userId;
    }

    private void setUserId(@StringRes int prefKey, String userId) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(context.getString(prefKey), userId);
        editor.commit();
    }

    public String getFacebookToken() {
        return getToken(R.string.pref_facebook_token, "");
    }

    public String getGoogleToken() {
        return getToken(R.string.pref_google_token, "");
    }

    public void setFacebookToken(String token) {
        setToken(R.string.pref_facebook_token, token);
    }

    public void setGoogleToken(String token) {
        setToken(R.string.pref_google_token, token);
    }

    public String getFacebookUserId() {
        return getUserId(R.string.pref_facebook_user_id);
    }

    public String getGoogleUserId() {
        return getUserId(R.string.pref_google_user_id);
    }

    public void setFacebookUserId(String userId) {
        setUserId(R.string.pref_facebook_user_id, userId);
    }

    public void setGoogleUserId(String userId) {
        setUserId(R.string.pref_google_user_id, userId);
    }

    /**
     * Gets the default anonymity or the the last anonymity setting used.
     * The default returned value is true.
     *
     * @return true if the user is anonymous.
     */
    public boolean getDefaultAnonymity() {
        boolean anonymity = true;

        if (mPrefs.contains(context.getString(R.string.pref_anonymity))) {
            anonymity = mPrefs.getBoolean(context.getString(R.string.pref_anonymity), true);
        }

        return anonymity;
    }

    public void setDefaultAnonymity(boolean anonymity) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(context.getString(R.string.pref_anonymity), anonymity);
        editor.commit();
    }
}
