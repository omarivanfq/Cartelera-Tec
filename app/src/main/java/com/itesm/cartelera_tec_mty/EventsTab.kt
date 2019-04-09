package com.itesm.cartelera_tec_mty

import NetworkUtility.NetworkConnection
import android.content.Context
import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray

class EventsTab : Fragment() {

    lateinit var eventsListView:ListView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.events_tab, container, false)
        eventsListView = rootView.findViewById<ListView>(R.id.events_list)
        loadEvents()
      //  loadEventsFromJson()
        return rootView
    }

    fun loadEventsFromJson() {
        val jsonString: String = loadJsonFromAsset("events.json", context)
        handleJson(jsonString)
    }

    fun loadJsonFromAsset(fileName: String, context: Context): String =
            (context.assets.open(fileName) ?: throw RuntimeException("Cannot open file: $fileName"))
                    .bufferedReader().use { it.readText() }

    fun loadEvents() {
        if (NetworkConnection.isNetworkConnected(activity)){
            doAsync {
                val url = NetworkConnection.buildEventsUrl()
                val dataJson = NetworkConnection.getResponseFromHttpUrl(url)
                uiThread {
                    handleJson(dataJson)
                }
            }
        }
    }

    private fun handleJson(jsonString: String?) {
        val jsonArray = JSONArray(jsonString)
        var list:MutableList<Event> = mutableListOf()
        var x = 0
        while(x < jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(x)
            list.add(Event(
                    jsonObject.getInt("id"),
                    jsonObject.getString("photo"),
                    jsonObject.getString("name"),
                    jsonObject.getString("startDatetime"),
                    jsonObject.getString("location"),
                    jsonObject.getInt("sponsorId"),
                    jsonObject.getBoolean("cancelled"),
                    jsonObject.getString("description"),
                    jsonObject.getString("campus"),
                    jsonObject.getString("category"),
                    jsonObject.getString("categoryName"),
                    jsonObject.getDouble("cost"),
                    jsonObject.getBoolean("publicEvent"),
                    jsonObject.getString("endDatetime"),
                    jsonObject.getString("requirementsToRegister"),
                    jsonObject.getString("registrationUrl"),
                    jsonObject.getString("registrationDeadline"),
                    jsonObject.getString("schedule"),
                    jsonObject.getString("facebookUrl"),
                    jsonObject.getString("twitterUrl"),
                    jsonObject.getString("contactPhone"),
                    jsonObject.getString("contactEmail"),
                    jsonObject.getString("contactName"),
                    jsonObject.getBoolean("published"),
                    jsonObject.getString("cancelMessage"),
                    jsonObject.getString("languages"),
                    jsonObject.getString("prefix"),
                    jsonObject.getBoolean("hasRegistration"),
                    jsonObject.getBoolean("petFriendly"),
                    jsonObject.getString("majors"),
                    jsonObject.getBoolean("hasDeadline"),
                    jsonObject.getString("registrationMessage"),
                    jsonObject.getString("tagNames"),
                    jsonObject.getInt("maxCapacity"),
                    jsonObject.getInt("categoryId"),
                    jsonObject.getInt("registeredCount"),
                    jsonObject.getDouble("latitude"),
                    jsonObject.getDouble("longitude"),
                    jsonObject.getString("city"),
                    jsonObject.getString("state"),
                    jsonObject.getString("reviewStatus"),
                    jsonObject.getString("reviewComments"),
                    jsonObject.getInt("applicantId")))
            x++
        }

        list.sortBy { it.startDateTime }

        val adapter = EventAdapter(activity, list)
        eventsListView.adapter = adapter
    }

    companion object {
        const val EXTRA_EVENT = "extra_event"
    }

}
