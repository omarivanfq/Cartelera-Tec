package com.itesm.cartelera_tec_mty

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

class EventsTab : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.events_tab, container, false)
        val libros = Event.loadEvents()
        val adapter = EventAdapter(activity, libros)
        adapter.notifyDataSetChanged()
        val eventsListView = rootView.findViewById<ListView>(R.id.events_list)
        eventsListView.adapter = adapter
        return rootView
    }

}
