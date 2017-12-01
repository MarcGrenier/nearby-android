package io.nearby.android.data

import java.util.*

/**
 * Created by Marc on 2017-11-28
 */
data class Spotted(
        val id: String,
        val message: String,
        val location: Location) {

    var userId: String? = null
    var anonymity: Boolean = false
    var creationDate: Date? = null
    var thumbnailURL: String? = null
    var pictureURL: String? = null
    var fullName: String? = null
    var profilePictureURL: String? = null

    constructor(_id: String,
                message: String,
                location: Location,
                anonymous: Boolean) : this(_id, message, location) {
        this.anonymity = anonymous
    }

    override fun equals(other: Any?): Boolean {
        if (other is Spotted) {
            return other.id == id
        } else {
            return super.equals(other)
        }
    }

    companion object DummyFactory {
        fun create(): Spotted {

            val id = UUID.randomUUID().hashCode()
            val userId = (UUID.randomUUID().hashCode() % 100).toString()
            val message = getMessage(id)
            val location = getLocation(id)
            val anonymity = getAnonymity(id)

            val spotted = Spotted(id.toString(), message, location, anonymity)
            spotted.userId = userId
            spotted.fullName = getFullName(id)
            spotted.creationDate = getCreationDate(id)

            return spotted
        }

        private fun getMessage(id: Int): String {
            when (id % 10) {
                0 -> return "Message 0"
                1 -> return "Message 1"
                2 -> return "Message 2"
                3 -> return "Message 3"
                4 -> return "Message 4"
                5 -> return "Message 5"
                6 -> return "Message 6"
                7 -> return "Message 7"
                8 -> return "Message 8"
                9 -> return "Message 9"
                else -> return "Message 10"
            }
        }

        private fun getLocation(id: Int): Location {
            val random = Random(id.toLong())
            val longitude = -180 + 360 * random.nextDouble()
            val latitude = -180 + 360 * random.nextDouble()

            return Location(latitude, longitude)
        }

        private fun getAnonymity(id: Int): Boolean {
            return id % 2 == 0
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

        private fun getCreationDate(id: Int) : Date{
            val random = Random(id.toLong())
            return Date(random.nextLong())
        }
    }
}
