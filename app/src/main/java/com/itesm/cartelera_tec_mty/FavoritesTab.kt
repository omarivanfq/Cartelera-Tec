package com.itesm.cartelera_tec_mty

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

class FavoritesTab : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.favorites_tab, container, false)
        //val libros = Event.loadFavoriteEvents(activity)
       // val adapter = EventAdapter(activity, libros)
       // adapter.notifyDataSetChanged()
       // val eventsListView = rootView.findViewById<ListView>(R.id.favorites_list)
       // eventsListView.adapter = adapter
        return rootView
    }
}