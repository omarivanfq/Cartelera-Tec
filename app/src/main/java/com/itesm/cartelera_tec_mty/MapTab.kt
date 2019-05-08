package com.itesm.cartelera_tec_mty

import android.Manifest
import android.app.FragmentManager
import android.content.Context
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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.CameraPosition



class MapTab : SupportMapFragment(), OnMapReadyCallback, MainActivity.OnDataPassedListener {

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
        for (event in events) {
            mMap?.addMarker(MarkerOptions().position(LatLng(event.latitude, event.longitude)).title(event.name))
        }
        mMap?.moveCamera(CameraUpdateFactory.newCameraPosition(tec))
    }

    override fun onDataPassed(events: MutableList<Event>) {

        //val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        //mapFragment?.getMapAsync(this)
        mMap?.clear()
        for (event in events) {
            mMap?.addMarker(MarkerOptions().position(LatLng(event.latitude, event.longitude)).title(event.name))
        }
    }
}
