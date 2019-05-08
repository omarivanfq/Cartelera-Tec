package com.itesm.cartelera_tec_mty

import android.Manifest
import android.app.FragmentManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.text.SimpleDateFormat
import java.util.*


class MapTab : SupportMapFragment(), OnMapReadyCallback, MainActivity.OnDataPassedListener,
        GoogleMap.OnMarkerClickListener {
    override fun onMarkerClick(p0: Marker?):Boolean {
        var events = (activity as MainActivity).events
        for (event in events) {
            if(event.name == p0?.title) {
                val detailIntent = Intent(context, EventDetail::class.java)
                detailIntent.putExtra(EventsTab.EXTRA_EVENT, event)
                ContextCompat.startActivity(context, detailIntent, null)
            }
        }
        return false
    }


    private var mMap:GoogleMap? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.map_tab, container, false)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager.
                findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        return view
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            println("@@@")
            mMap?.setMyLocationEnabled(true)
        }
        var events = (activity as MainActivity).events
        val tec = CameraPosition.Builder()
                .target(LatLng(25.651115, -100.289370))
                .bearing(48f).tilt(0f).zoom(16.9f).build()
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS-HH:mm")
        val outputFormat = SimpleDateFormat("dd-MM-yyyy")
        for (event in events) {
            val eventDate = inputFormat.parse(event.startDateTime)
            val formattedDate = outputFormat.format(eventDate)
            if(formattedDate == outputFormat.format(Date())) {
                println(Date())
                mMap?.addMarker(MarkerOptions().position(LatLng(event.latitude, event.longitude))
                        .title(event.name)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)))
            } else {
                mMap?.addMarker(MarkerOptions().position(LatLng(event.latitude, event.longitude))
                        .title(event.name))
            }
        }
        mMap?.moveCamera(CameraUpdateFactory.newCameraPosition(tec))
        mMap?.setOnMarkerClickListener(this)
    }

    override fun onDataPassed(events: MutableList<Event>) {

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS-HH:mm")
        val outputFormat = SimpleDateFormat("dd-MM-yyyy")
        mMap?.clear()
        for (event in events) {
            val eventDate = inputFormat.parse(event.startDateTime)
            val formattedDate = outputFormat.format(eventDate)
            if(formattedDate == outputFormat.format(Date())) {
                println(Date())
                mMap?.addMarker(MarkerOptions().position(LatLng(event.latitude, event.longitude))
                        .title(event.name)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)))
            } else {
                mMap?.addMarker(MarkerOptions().position(LatLng(event.latitude, event.longitude))
                        .title(event.name))
            }
        }
    }
}
