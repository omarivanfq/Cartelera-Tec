package com.itesm.cartelera_tec_mty

import TimeUtility.TimeFormat
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import java.text.SimpleDateFormat

class EventAdapter (val context: Context, val list:MutableList<Event>):BaseAdapter() {

    /*
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater: LayoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.event_row, parent, false)
        bind(view, list[position])
        return view
    }
*/
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val eventHolder:EventViewHolder
        val rowView:View
        val event:Event = getItem(position) as Event

        if (convertView == null) {
            val inflater: LayoutInflater =
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            rowView = inflater.inflate(R.layout.event_row, parent, false)
            eventHolder = EventViewHolder(rowView)
            rowView.tag = eventHolder
        } else {
            rowView = convertView
            eventHolder = convertView.tag as EventViewHolder
        }
        eventHolder.bind(event)
        return rowView
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

    /*
    fun bind(containerView: View, event: Event) {
        val textviewTitle = containerView.findViewById<TextView>(R.id.textview_title)
        val textviewLocation = containerView.findViewById<TextView>(R.id.textview_location)
        val textviewDate = containerView.findViewById<TextView>(R.id.textview_date)
        val textviewTime = containerView.findViewById<TextView>(R.id.textview_time)
        val imageview = containerView.findViewById<ImageView>(R.id.image_event)
        textviewTitle.text = event.name //textview_nombre.text = libro.nombre
        textviewLocation.text = event.location
        textviewDate.text = TimeFormat.getDate(event.startDateTime)
        textviewTime.text = TimeFormat.getTime(event.startDateTime) + " - " + TimeFormat.getTime(event.endDateTime)
      //  imageview.setImageResource(R.drawable.event_1_pic)
        Picasso.get().load(event.photo).into(imageview)
        containerView.setOnClickListener { _ ->
            val detailIntent = Intent(context, EventDetail::class.java)
            detailIntent.putExtra(EventsTab.EXTRA_EVENT, event)
            startActivity(context, detailIntent, null)
        }
    }*/

    inner class EventViewHolder(override val containerView: View) : LayoutContainer {
        fun bind(event: Event) {
            val textviewTitle = containerView.findViewById<TextView>(R.id.textview_title)
            val textviewLocation = containerView.findViewById<TextView>(R.id.textview_location)
            val textviewDate = containerView.findViewById<TextView>(R.id.textview_date)
            val textviewTime = containerView.findViewById<TextView>(R.id.textview_time)
            val imageview = containerView.findViewById<ImageView>(R.id.image_event)
            textviewTitle.text = event.name //textview_nombre.text = libro.nombre
            textviewLocation.text = event.location
            textviewDate.text = TimeFormat.getDate(event.startDateTime)
            textviewTime.text = TimeFormat.getTime(event.startDateTime) + " - " + TimeFormat.getTime(event.endDateTime)
            Picasso.get().load(event.photo).into(imageview)
            containerView.setOnClickListener { _ ->
                val detailIntent = Intent(context, EventDetail::class.java)
                detailIntent.putExtra(EventsTab.EXTRA_EVENT, event)
                startActivity(context, detailIntent, null)
            }
        }
    }


}