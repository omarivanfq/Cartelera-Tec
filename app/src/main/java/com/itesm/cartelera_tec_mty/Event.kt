package com.itesm.cartelera_tec_mty

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Event(val title:String,
            val location:String,
            val date:String,
            val time:String,
            val idImage:Int): Parcelable {
    companion object {

        val titles: Array<String> = arrayOf("TALLER Fab Lat Kids", "TALLER Fab Lat Kids")
        val locations: Array<String> = arrayOf("Innovaction Gym", "Innovaction Gym")
        val dates: Array<String> = arrayOf("4 mayo 2019", "4 mayo 2019")
        val times: Array<String> = arrayOf("10:00 - 13:00", "10:00 - 13:00")
        val idImages: Array<Int>  = arrayOf(R.drawable.event_1_pic, R.drawable.event_1_pic)

        @SuppressLint("ResourceType", "Recycle")
        fun loadEvents(): MutableList<Event> {
            val eventsList: MutableList<Event> = mutableListOf()
            (0 until titles.size).mapTo(eventsList) {
                Event(
                        titles[it],
                        locations[it],
                        dates[it],
                        times[it],
                        idImages[it]
                )
            }
            return eventsList
        }
    }
}