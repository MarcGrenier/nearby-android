package io.nearby.android.data

import java.util.*

/**
 * Created by Marc on 2017-11-28
 */
data class Spotted(
        val id: String,
        val message: String,
        val location : Location1) {

    private var userId: String = ""
    private var anonymity: Boolean = false
    private var creationDate: Date? = null
    private var thumbnailURL: String = ""
    private var pictureURL: String = ""
    private var fullName: String = ""
    private var profilePictureURL: String = ""

    constructor(_id: String,
                message: String,
                location : Location1,
                anonymous : Boolean) : this(_id, message, location){
        this.anonymity = anonymous
    }

    override fun equals(other: Any?): Boolean {
        if (other is Spotted) {
            return other.id == id
        } else {
            return super.equals(other)
        }
    }
}
