package io.nearby.android.data.model

import java.util.*

/**
 * Created by Marc on 2017-11-28
 */
data class User(
        val id: String,
        val creationDate: Date,
        val disabled: Boolean,
        val facebookId: String?,
        val facebookDate: Date?,
        val googleId: String?,
        val fullName: String,
        val profilePictureURL: String?) {

    fun hasFacebookAccount(): Boolean {
        return !facebookId.isNullOrEmpty()
    }

    fun hasGoogleAccount(): Boolean {
        return !googleId.isNullOrEmpty()
    }

    companion object DummyFactory {
        fun create(): User {
            val id = UUID.randomUUID().hashCode()
            val userId = (UUID.randomUUID().hashCode() % 100).toString()
            return User(userId,
                    getCreationDate(id),
                    false,
                    null,
                    null,
                    null,
                    getFullName(id),
                    null)
        }

        private fun getCreationDate(id: Int) : Date{
            val random = Random(id.toLong())
            return Date(random.nextLong())
        }

        private fun getFullName(id: Int): String {
            var firstName = ""
            var lastName = ""

            when (id % 10){
                0 -> firstName = "Marc"
                1 -> firstName = "Mark"
                2 -> firstName = "Christian"
                3 -> firstName = "Tiffany"
                4 -> firstName = "Fred"
                5 -> firstName = "Bob"
                6 -> firstName = "Jean"
                7 -> firstName = "Joe"
                8 -> firstName = "Gaspard"
                9 -> firstName = "Tom"
            }

            when ((id/10) % 10){
                0 -> lastName = "Bloe"
                1 -> lastName = "Smith"
                2 -> lastName = "Cruise"
                3 -> lastName = "Tremblay"
                4 -> lastName = "Brown"
                5 -> lastName = "Jones"
                6 -> lastName = "Johnson"
                7 -> lastName = "William"
                8 -> lastName = "Miller"
                9 -> lastName = "White"
            }

            return firstName + " " + lastName
        }
    }
}