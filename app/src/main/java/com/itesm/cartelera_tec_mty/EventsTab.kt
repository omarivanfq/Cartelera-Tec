package com.itesm.cartelera_tec_mty

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.*
import android.widget.ListView

class EventsTab : Fragment() {

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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
    }

}
