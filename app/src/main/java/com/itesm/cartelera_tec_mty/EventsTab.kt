package com.itesm.cartelera_tec_mty

import android.os.AsyncTask
import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat

class EventsTab : Fragment() {

    lateinit var eventsListView:ListView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.events_tab, container, false)
      //  val libros = Event.loadEvents()
      //  val adapter = EventAdapter(activity, libros)
      //  adapter.notifyDataSetChanged()
        eventsListView = rootView.findViewById<ListView>(R.id.events_list)
       // eventsListView.adapter = adapter

        val url = "https://cartelerai-api.herokuapp.com/events"
        AsyncTaskHandleJson().execute(url)

        return rootView
    }

    inner class AsyncTaskHandleJson : AsyncTask<String, String, String>(){
        override fun doInBackground(vararg url: String?): String {
            val text: String
            val connection = URL(url[0]).openConnection() as HttpURLConnection
            try{
                connection.connect()
                text = connection.inputStream.use { it.reader().use{reader -> reader.readText()}}
            } finally {
                connection.disconnect()
            }
            return text
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            handleJson(result)
        }
    }

    private fun handleJson(jsonString: String?) {
        val jsonArray = JSONArray(jsonString)
        val list = ArrayList<Event>()
        var x = 0
        while(x < jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(x)
            //    .parse("2019-05-04T13:00:00.000-05:00")

            //val stringDate = jsonObject.getString("startDatetime")

            list.add(Event(
                    jsonObject.getInt("id"),
                    jsonObject.getString("photo"),
                    jsonObject.getString("name"),
                    jsonObject.getString("startDatetime"),
                    //"05-05-2019",
                    //startDateString,
                    jsonObject.getString("location"),
                    jsonObject.getInt("sponsorId"),
                    jsonObject.getBoolean("cancelled"),
                    jsonObject.getString("description"),
                    jsonObject.getString("campus"),
                    jsonObject.getString("category"),
                    jsonObject.getString("categoryName"),
                    jsonObject.getDouble("cost"),
                    jsonObject.getBoolean("publicEvent"),
                   // jsonObject.getString("endDateTime"),
                    jsonObject.getString("endDatetime"),
                   // endDateString,
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

        val adapter = EventAdapter(activity, list)
        eventsListView.adapter = adapter
    }

}
