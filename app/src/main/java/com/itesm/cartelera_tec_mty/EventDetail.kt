package com.itesm.cartelera_tec_mty

import Database.EventDatabase
import TimeUtility.TimeFormat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_event_detail.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class EventDetail : AppCompatActivity() {

    lateinit var event:Event
    lateinit var instanceDatabase: EventDatabase
    var favorite:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
        event = intent.extras.getParcelable<Event>(EventsTab.EXTRA_EVENT)
        bind(event)

        fab_favorite.setOnClickListener {
            if (favorite)
                deleteFromFavoriteDB()
            else
                addToFavoriteDB()
        }

        instanceDatabase = EventDatabase.getInstance(this)

        doAsync {
            // looking for the current event in the db
            val eventsCount = instanceDatabase.eventDao().eventsCount(event.id)
            uiThread {
                // if it isn't there then the favorite button must be empty
                if (eventsCount == 0){
                    favorite = false
                    fab_favorite.setImageDrawable(ContextCompat.getDrawable(this@EventDetail, R.drawable.ic_favorite))
                }
                // if it is there then the favorite button must be filled
                else {
                    favorite = true
                    fab_favorite.setImageDrawable(ContextCompat.getDrawable(this@EventDetail, R.drawable.ic_favorite_filled))
                }
            }
        }
    }

    // function that removes the current event from the database and updates the fab button
    fun deleteFromFavoriteDB(){
        doAsync {
            instanceDatabase.eventDao().deleteEvent(event.id)
            uiThread {
                Toast.makeText(this@EventDetail, "Deleted from favorites", Toast.LENGTH_SHORT).show()
                fab_favorite.setImageDrawable(ContextCompat.getDrawable(this@EventDetail, R.drawable.ic_favorite))
                favorite = false
            }
        }
    }

    // function that adds the current event to the database and updates the fab button
    fun addToFavoriteDB() {
        doAsync {
            instanceDatabase.eventDao().insertEvent(event)
            uiThread {
                Toast.makeText(this@EventDetail, "Added to favorites", Toast.LENGTH_SHORT).show()
                fab_favorite.setImageDrawable(ContextCompat.getDrawable(this@EventDetail, R.drawable.ic_favorite_filled))
                favorite = true
            }
        }
    }

    fun bind(event: Event){
        textview_title.text = event.name
        textview_sponsor.text = "Evento de " + event.sponsorId
        textview_location.text = event.location
        textview_date.text = TimeFormat.getDate(event.startDateTime)
        textview_time.text = TimeFormat.getTime(event.startDateTime) + " - " + TimeFormat.getTime(event.endDateTime)
        textview_description.text = event.description
        textview_email.text = event.contactEmail
        textview_fb.text = event.facebookUrl
        Picasso.get().load(event.photo).into(imageview_photo)
        textview_cost.text = "Costo: $" + event.cost
    }

}
