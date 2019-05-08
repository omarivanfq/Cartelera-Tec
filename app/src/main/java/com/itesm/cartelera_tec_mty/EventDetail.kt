package com.itesm.cartelera_tec_mty

import Database.EventDatabase
import TimeUtility.TimeFormat
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_event_detail.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File
import java.io.FileOutputStream
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*


class EventDetail : AppCompatActivity(), OnMapReadyCallback {

    lateinit var event:Event
    lateinit var instanceDatabase: EventDatabase
    var favorite:Boolean = false
    private var mapView:MapView? = null
    private var mMap:GoogleMap? = null
    private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
        event = intent.extras.getParcelable(EventsTab.EXTRA_EVENT)
        bind(event)

        //share on button click
        shareBtn.setOnClickListener{
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, event.description + "\nContacto: " + event.contactEmail)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, event.name)
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)))
        }

        //calendario
        calendarBtn.setOnClickListener{
            val title = textview_title.text.toString()
            val description = textview_description.text.toString()
            val location = textview_location.text.toString()
            val intent = Intent(Intent.ACTION_INSERT)
            intent.type = "vnd.android.cursor.item/event"
            intent.putExtra(CalendarContract.Events.TITLE, title)
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, TimeFormat.getDate(event.startDateTime))
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, TimeFormat.getDate(event.endDateTime))
            intent.putExtra(CalendarContract.Events.ALL_DAY, true)// periodicity
            intent.putExtra(CalendarContract.Events.DESCRIPTION, description)
            intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location)
            startActivity(intent)
        }

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

        var mapViewBundle:Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        // MapView
        mapView = findViewById(R.id.map_view)
        mapView?.onCreate(mapViewBundle)
        mapView?.getMapAsync(this)
    }

    // function that removes the current event from the database and updates the fab button
    fun deleteFromFavoriteDB(){
        doAsync {
            instanceDatabase.eventDao().deleteEvent(event.id)
            uiThread {
                Toast.makeText(this@EventDetail, getString(R.string.removed_favorite), Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this@EventDetail, getString(R.string.added_favorite), Toast.LENGTH_SHORT).show()
                fab_favorite.setImageDrawable(ContextCompat.getDrawable(this@EventDetail, R.drawable.ic_favorite_filled))
                favorite = true
            }
        }
    }

    fun bind(event: Event){
        textview_title.text = event.name
        textview_category.text = "Evento de ${event.categoryName}"
        textview_contact.text = "Creado por ${event.contactName}"
        textview_location.text = event.location
        textview_date.text = TimeFormat.getDate(event.startDateTime)
        textview_time.text = TimeFormat.getTime(event.startDateTime) + " - " + TimeFormat.getTime(event.endDateTime)
        textview_description.text = event.description
        textview_email.text = event.contactEmail
        textview_fb.text = event.facebookUrl
        Picasso.get().load(event.photo).into(imageview_photo)
        textview_cost.text = "Costo: $" + event.cost
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val tec = CameraPosition.Builder()
                .target(LatLng(25.651115, -100.289370))
                .bearing(48f).tilt(0f).zoom(16.6f).build()
        mMap?.addMarker(MarkerOptions().position(LatLng(event.latitude, event.longitude)).title(event.name))
        mMap?.moveCamera(CameraUpdateFactory.newCameraPosition(tec))
    }
    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle)
        }

        mapView?.onSaveInstanceState(mapViewBundle)
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onPause() {
        mapView?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mapView?.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }
}
