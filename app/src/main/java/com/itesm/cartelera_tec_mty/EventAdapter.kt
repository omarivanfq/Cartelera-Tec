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
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class EventAdapter (val context: Context, val list:ArrayList<Event>):BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
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
      //  imageview.setImageResource(R.drawable.event_1_pic)
        Picasso.get().load(event.photo).into(imageview)
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