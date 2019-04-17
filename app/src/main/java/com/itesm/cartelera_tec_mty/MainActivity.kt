package com.itesm.cartelera_tec_mty

import Database.EventDatabase
import NetworkUtility.NetworkConnection
import android.content.Context
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    lateinit var optionsMenu:Menu
    lateinit var unfilteredEvents:MutableList<Event>
    private var searchView: SearchView? = null
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    lateinit var dataJson:String
    lateinit var eventAdapter:EventAdapter
    lateinit var favoritesAdapter:EventAdapter

    lateinit var events:MutableList<Event>
    lateinit var favoriteEvents:MutableList<Event>

    lateinit var listIds:List<Int>
    lateinit var instanceDatabase: EventDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        unfilteredEvents = mutableListOf()
        events = mutableListOf()
        favoriteEvents = mutableListOf()
        eventAdapter = EventAdapter(this, events)
        favoritesAdapter = EventAdapter(this, favoriteEvents)
        instanceDatabase = EventDatabase.getInstance(this)

        setSupportActionBar(toolbar)
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        container.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
           //     Toast.makeText(this@MainActivity, "onPageScrollStateChanged", Toast.LENGTH_SHORT).show()
            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
         //       Toast.makeText(this@MainActivity, "onPageScrolled", Toast.LENGTH_SHORT).show()
            }
            override fun onPageSelected(position: Int) {
                optionsMenu.findItem(R.id.item_search).isVisible = position == 0
                Toast.makeText(this@MainActivity, "onPageSelected $position", Toast.LENGTH_SHORT).show()
            }

        })

        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

//        loadEvents() // load from web service
        loadEventsFromJson() // loading dummy data
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        Toast.makeText(this, "onPrepareOptionsMenu", Toast.LENGTH_SHORT).show()
        return super.onPrepareOptionsMenu(menu)
    }

    fun doMySearch(query:String) = unfilteredEvents.filter { event -> event.name.contains(query,true) }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        optionsMenu = menu
        menuInflater.inflate(R.menu.menu_search, menu)
        searchView = menu.findItem(R.id.item_search).actionView as SearchView

        // assigns a hint into SearchView query text
        searchView?.queryHint = getString(R.string.search_hint)

        // TODO 9: Para detectar que se presiona el botón de back del toolbar
        searchView?.setOnQueryTextFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(view: View, queryTextFocused: Boolean) {
                if (!queryTextFocused) {
                    searchView?.setIconified(true)
                }
            }
        })

        // TODO 8: Actualizar lista con todos los elementos al cerrar el SearchView
        searchView?.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                events.clear()
                events.addAll(unfilteredEvents)
                eventAdapter.notifyDataSetChanged()
                searchView?.onActionViewCollapsed()
                supportInvalidateOptionsMenu()
                return true
            }
        })

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                val newEvents:MutableList<Event> = mutableListOf()
                newEvents.addAll(doMySearch(query).toMutableList())
                events.clear()
                events.addAll(newEvents)
                eventAdapter.notifyDataSetChanged()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            val eventsTab = EventsTab()
            eventsTab.adapter = eventAdapter
            val favoritesTab = FavoritesTab()
            favoritesTab.adapter = favoritesAdapter
            return when (position) {
                0 -> eventsTab
                1 -> favoritesTab
                2 -> SearchTab()
                3 -> MapTab()
                else -> SearchTab()
            }
        }
        override fun getCount(): Int {
            return 4
        }



    }
    fun loadJsonFromAsset(fileName: String, context: Context): String =
            (context.assets.open(fileName) ?: throw RuntimeException("Cannot open file: $fileName"))
                    .bufferedReader().use { it.readText() }
    // function that loads the events from the JSON file
    fun loadEventsFromJson() {
        if (NetworkConnection.isNetworkConnected(this)) {
            val jsonString: String = loadJsonFromAsset("events.json", this)
            handleJson(jsonString)
        }
    }
    // function that loads the events from the web service
    fun loadEvents() {
        if (NetworkConnection.isNetworkConnected(this)){
            doAsync {
                val url = NetworkConnection.buildEventsUrl()
                 dataJson = NetworkConnection.getResponseFromHttpUrl(url)
                uiThread {
                    handleJson(dataJson)
                }
            }
        }
    }
    // function that receives the JSON array data and converts it into a mutable list
    // of events that is assigned to the listview adapter
    private fun handleJson(jsonString: String?) {
        val jsonArray = JSONArray(jsonString)
        val list:MutableList<Event> = mutableListOf()
        var x = 0
        while (x < jsonArray.length()){
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

            events.add(list.last())
            x++
        }
        Toast.makeText(this@MainActivity, "All events loaded", Toast.LENGTH_SHORT).show()
        events.sortBy {it.startDateTime}
        unfilteredEvents.addAll(events)
        eventAdapter.notifyDataSetChanged()
        updateFavoritesListData()
        favoritesAdapter.notifyDataSetChanged()
        if (events.isEmpty()) {
            textview_noevents.visibility = View.VISIBLE
        }
        else {
            textview_noevents.visibility = View.INVISIBLE
        }
    }
    override fun onResume() {
        super.onResume()
        updateFavoritesListData()
    }
    private fun updateFavoritesListData(){
        doAsync {
            listIds = instanceDatabase.eventDao().loadIds()
            uiThread {
                if (NetworkConnection.isNetworkConnected(this@MainActivity)){
                    val favEvents = events.filter { it.id in listIds }
                    favoriteEvents.clear()
                    favoriteEvents.addAll(favEvents)
                    favoritesAdapter.notifyDataSetChanged()
                }
                else {
                    doAsync {
                        val favEvents = instanceDatabase.eventDao().loadAllEvents()
                        uiThread {
                            favoriteEvents.clear()
                            favoriteEvents.addAll(favEvents)
                            favoritesAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }
}
