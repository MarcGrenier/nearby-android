package io.nearby.android.data.manager

/**
 * Created by Marc on 2017-12-16
 */

data class LoginResult(var status: Status) {

    enum class Status {
        LOGGED_IN,
        ACCOUNT_CREATED,
        NOT_LOGGED_IN
    }
}
