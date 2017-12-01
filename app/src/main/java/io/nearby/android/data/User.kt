package io.nearby.android.data

import java.util.*

/**
 * Created by Marc on 2017-11-28
 */
data class User(
        val _id: String,
        val creationDate: Date,
        val disabled: Boolean,
        val facebookId: String?,
        val facebookDate: Date,
        val googleId: String?,
        val fullName: String,
        val profilePictureURL: String?) {

    fun hasFacebookAccount(): Boolean {
        return !facebookId.isNullOrEmpty()
    }

    fun hasGoogleAccount(): Boolean {
        return !googleId.isNullOrEmpty()
    }
}