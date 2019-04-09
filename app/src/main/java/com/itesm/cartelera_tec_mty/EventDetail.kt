package com.itesm.cartelera_tec_mty

import TimeUtility.TimeFormat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_event_detail.*

class EventDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val event = intent.extras.getParcelable<Event>(EventsTab.EXTRA_EVENT)
        setContentView(R.layout.activity_event_detail)
        bind(event)
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
