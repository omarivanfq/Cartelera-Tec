package com.itesm.cartelera_tec_mty


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
import java.text.SimpleDateFormat

class EventAdapter (val context: Context, val list:ArrayList<Event>):BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
    //    val view: View = LayoutInflater.from(context).inflate(R.layout.event_row, parent)
        val inflater: LayoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.event_row, parent, false)
        bind(view, list[position])
        return view
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

    fun bind(containerView: View, event: Event) {
        val textviewTitle = containerView.findViewById<TextView>(R.id.textview_title)
        val textviewLocation = containerView.findViewById<TextView>(R.id.textview_location)
        val textviewDate = containerView.findViewById<TextView>(R.id.textview_date)
        val textviewTime = containerView.findViewById<TextView>(R.id.textview_time)
        val imageview = containerView.findViewById<ImageView>(R.id.image_event)
        textviewTitle.text = event.name //textview_nombre.text = libro.nombre
        textviewLocation.text = event.location
        textviewDate.text = getDate(event.startDateTime)
        textviewTime.text = getTime(event.startDateTime) + " - " + getTime(event.endDateTime)
        imageview.setImageResource(R.drawable.event_1_pic)
        containerView.setOnClickListener { _ ->
            val detailIntent = Intent(context, EventDetail::class.java)
            startActivity(context, detailIntent, null)
        }

    }

    fun getDate(sdatetime:String):String {
        val dateTimeParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss-mm:ss")
        val dateTime = dateTimeParser.parse(sdatetime)
        val dateFormat = SimpleDateFormat("dd MMM yyyy")
        return dateFormat.format(dateTime)
    }

    fun getTime(sdatetime:String):String {
        val dateTimeParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss-mm:ss")
        val dateTime = dateTimeParser.parse(sdatetime)
        val dateFormat = SimpleDateFormat("HH:mm")
        return dateFormat.format(dateTime)
    }

}

/*
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.extensions.LayoutContainer

class EventAdapter(private val context: Context,
                   private val libros: MutableList<Event>): BaseAdapter() {

    //var viewHolderCount:Int = 0
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val eventHolder:EventViewHolder
        val rowView: View
        val event:Event = getItem(position) as Event

        // si no existe una vista para el renglón,se crea
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
        return libros[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return libros.size
    }

    inner class EventViewHolder(override val containerView: View) : LayoutContainer {
        fun bind(event: Event) {
            val textviewTitle = containerView.findViewById<TextView>(R.id.textview_title)
            val textviewLocation = containerView.findViewById<TextView>(R.id.textview_location)
            val textviewDate = containerView.findViewById<TextView>(R.id.textview_date)
            val textviewTime = containerView.findViewById<TextView>(R.id.textview_time)
            val imageview = containerView.findViewById<ImageView>(R.id.image_event)
            textviewTitle.text = event.title //textview_nombre.text = libro.nombre
            textviewLocation.text = event.location
            textviewDate.text = event.date
            textviewTime.text = event.time
            imageview.setImageResource(R.drawable.event_1_pic)
            containerView.setOnClickListener { _ ->
                val detailIntent = Intent(context, EventDetail::class.java)
                startActivity(context, detailIntent, null)
            }
        }
    }

    interface CustomOnItemClickListener {
        fun onCustomItemClick(event: Event)
    }
}

*/
