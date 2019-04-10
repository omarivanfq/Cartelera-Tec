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

    lateinit var adapter:EventAdapter
    lateinit var listIds:List<Int>
    lateinit var instanceDatabase:EventDatabase
    lateinit var favoriteEvents:MutableList<Event>
    lateinit var nmberIds:List<Int>
    lateinit var listView:ListView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.favorites_tab, container, false)
        instanceDatabase = EventDatabase.getInstance(context)
        listView = rootView.findViewById(R.id.favorites_list)
        return rootView
    }

    fun updateListData(){
        doAsync {
            listIds = instanceDatabase.eventDao().loadIds()
            uiThread {
                if (NetworkConnection.isNetworkConnected(context)){
                    val favEvents = MainActivity.events.filter { it.id in listIds }
                    adapter = EventAdapter(context, favEvents.toMutableList())
                    listView.adapter = adapter
                }
                else {
                    doAsync {
                        val favEvents = instanceDatabase.eventDao().loadAllEvents()
                        uiThread {
                            adapter = EventAdapter(context, favEvents.toMutableList())
                            listView.adapter = adapter
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateListData()
    }

}