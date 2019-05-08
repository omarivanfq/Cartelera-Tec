package com.itesm.cartelera_tec_mty

import Database.EventDatabase
import NetworkUtility.NetworkConnection
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class FavoritesTab : Fragment() {

    var adapter:EventAdapter? = null
    lateinit var listView:ListView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.favorites_tab, container, false)
        listView = rootView.findViewById(R.id.favorites_list)
        listView.adapter = (activity as MainActivity).favoritesAdapter
        return rootView
    }

}