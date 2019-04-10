package com.itesm.cartelera_tec_mty

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "Event")
@Parcelize
data class Event(@ColumnInfo(name = "_id") @PrimaryKey(autoGenerate = false) val id: Int, val photo: String, val name: String, val startDateTime : String, val location: String, val sponsorId: Int, val cancelled: Boolean,
                 val description: String, val campus: String, val category: String, val categoryName: String, val cost: Double, val publicEvent: Boolean,
                 val endDateTime : String, val requirementsToRegister: String, val registrationUrl: String, val registrationDeadline: String, val schedule: String,
                 val facebookUrl:String, val twitterUrl:String, val contactPhone:String, val contactEmail:String, val contactName:String, val published:Boolean,
                 val cancelMessage:String, val languages: String, val prefix:String, val hasRegistration:Boolean, val petFriendly:Boolean, val majors:String,
                 val hasDeadline:Boolean, val registrationMessage:String, val tagNames:String, val maxCapacity:Int, val categoryId:Int, val registeredCount: Int,
                 val latitude:Double, val longitude:Double, val city:String, val state:String, val reviewStatus:String, val reviewComments:String, val applicantId:Int)
            : Parcelable {

   /* companion object {

        val ids: Array<Int> = arrayOf(151, 152, 153, 154, 155, 156, 157, 158)
        val titles: Array<String> = arrayOf("TALLER Fab Lat Kids", "TALLER Fab Lat Kids", "TALLER Fab Lat Kids", "TALLER Fab Lat Kids", "TALLER Fab Lat Kids", "TALLER Fab Lat Kids", "TALLER Fab Lat Kids", "TALLER Fab Lat Kids")
        val locations: Array<String> = arrayOf("Innovaction Gym", "Innovaction Gym", "Innovaction Gym", "Innovaction Gym", "Innovaction Gym", "Innovaction Gym", "Innovaction Gym", "Innovaction Gym")
        val dates: Array<String> = arrayOf("4 mayo 2019", "4 mayo 2019", "4 mayo 2019", "4 mayo 2019", "4 mayo 2019", "4 mayo 2019", "4 mayo 2019", "4 mayo 2019")
        val times: Array<String> = arrayOf("10:00 - 13:00", "10:00 - 13:00", "10:00 - 13:00", "10:00 - 13:00", "10:00 - 13:00", "10:00 - 13:00", "10:00 - 13:00", "10:00 - 13:00")
        val idImages: Array<Int>  = arrayOf(R.drawable.event_1_pic, R.drawable.event_1_pic, R.drawable.event_1_pic, R.drawable.event_1_pic, R.drawable.event_1_pic, R.drawable.event_1_pic, R.drawable.event_1_pic, R.drawable.event_1_pic)

        @SuppressLint("ResourceType", "Recycle")
        fun loadEvents(): MutableList<Event> {
            val eventsList: MutableList<Event> = mutableListOf()
            (0 until titles.size).mapTo(eventsList) {
                Event(
                        ids[it],
                        titles[it],
                        locations[it],
                        dates[it],
                        times[it],
                        idImages[it]
                )
            }
            return eventsList
        }

        @SuppressLint("ResourceType", "Recycle")
        fun loadFavoriteEvents(context: Context): MutableList<Event> {
            val eventsList = loadEvents()
            val favoriteEventsList: MutableList<Event> = mutableListOf()
            val favoritesIds = context.resources.getIntArray(R.array.favorites)

            for (event in eventsList) {
                if (favoritesIds.contains(event.id)) {
                    favoriteEventsList.add(event)
                }
            }
            return favoriteEventsList
        }

    }
    */
}