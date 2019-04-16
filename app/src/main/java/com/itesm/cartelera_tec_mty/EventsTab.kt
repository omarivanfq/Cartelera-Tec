package com.itesm.cartelera_tec_mty

import android.app.SearchManager
import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.ListView

class EventsTab : Fragment() {

    private var searchView: SearchView? = null
    lateinit var eventsListView:ListView
    lateinit var adapter:EventAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.events_tab, container, false)
        eventsListView = rootView.findViewById(R.id.events_list)
        eventsListView.adapter = adapter
        return rootView
    }

    companion object {
        const val EXTRA_EVENT = "extra_event"
    }

}
