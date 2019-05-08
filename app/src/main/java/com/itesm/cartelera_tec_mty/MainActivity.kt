package com.itesm.cartelera_tec_mty

import Database.EventDatabase
import NetworkUtility.NetworkConnection
import TimeUtility.TimeFormat
import android.Manifest
import android.content.Context
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.SearchView
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap

import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray

class MainActivity : AppCompatActivity(), Filters.FilteringListener {

    var optionsMenu:Menu? = null
    private lateinit var unfilteredEvents:MutableList<Event>
    private var searchView: SearchView? = null
    private var filterView: Button? = null
    var dataPasser: OnDataPassedListener? = null
    var mapFragment:MapTab? = null
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    lateinit var eventAdapter:EventAdapter
    lateinit var favoritesAdapter:EventAdapter
    lateinit var mMap: GoogleMap
    lateinit var events:MutableList<Event>
    private lateinit var favoriteEvents:MutableList<Event>

    private lateinit var listIds:List<Int>
    private lateinit var instanceDatabase: EventDatabase

    private lateinit var connectionProblemTextView:TextView
    private lateinit var filteredEventsTextView:TextView

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

        connectionProblemTextView = findViewById(R.id.textview_no_connection)
        filteredEventsTextView = findViewById(R.id.textview_filtered_events)

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        container.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                optionsMenu?.findItem(R.id.item_search)?.isVisible = position == 0
            }
        })

        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

//        loadEvents() // load from web service
        loadEventsFromJson() // loading dummy data
    }

    fun doMySearch(query:String) = unfilteredEvents.filter { event -> event.name.contains(query,true) }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null && item.itemId == R.id.item_filter) {
            drawer_layout.openDrawer(Gravity.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        optionsMenu = menu
        menuInflater.inflate(R.menu.menu_search, menu)
        searchView = menu.findItem(R.id.item_search).actionView as SearchView

        // assigns a hint into SearchView query text
        searchView?.queryHint = getString(R.string.search_hint)

        searchView?.setOnQueryTextFocusChangeListener { _, queryTextFocused ->
            if (!queryTextFocused) {
                searchView?.isIconified = true
            }
        }

        searchView?.setOnCloseListener {
            events.clear()
            events.addAll(unfilteredEvents)
            eventAdapter.notifyDataSetChanged()
            searchView?.onActionViewCollapsed()
            //supportInvalidateOptionsMenu()
            true
        }

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
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
            val mapTab = MapTab()
            mapFragment = mapTab
            return when (position) {
                0 -> eventsTab
                1 -> favoritesTab
             //   2 -> SearchTab()
                2 -> mapTab
                else -> SearchTab()
            }
        }
        override fun getCount(): Int {
            return 3
        }

    }
    private fun loadJsonFromAsset(fileName: String, context: Context): String =
            (context.assets.open(fileName) ?: throw RuntimeException("Cannot open file: $fileName"))
                    .bufferedReader().use { it.readText() }

    private fun showNoConnectionView(show:Boolean) {
        if (show) {
            connectionProblemTextView.visibility = View.VISIBLE
            showFilteredEventsView(false)
        }
        else {
            connectionProblemTextView.visibility = View.INVISIBLE
        }
    }

    private fun showFilteredEventsView(show:Boolean) {
        if (show) {
            filteredEventsTextView.visibility = View.VISIBLE
            showNoConnectionView(false)
        }
        else {
            filteredEventsTextView.visibility = View.INVISIBLE
        }
    }

    // function that loads the events from the JSON file
    private fun loadEventsFromJson() {
        if (NetworkConnection.isNetworkConnected(this)) {
            val jsonString: String = loadJsonFromAsset(fileName = "events.json", context = this)
            handleJson(jsonString)
            showNoConnectionView(false)
        }
        else {
            showNoConnectionView(true)
        }
    }
    // function that loads the events from the web service
    private fun loadEvents() {
        if (NetworkConnection.isNetworkConnected(this)){
            doAsync {
                val url = NetworkConnection.buildEventsUrl()
                val dataJson = NetworkConnection.getResponseFromHttpUrl(url)
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
//        Toast.makeText(this@MainActivity, "All events loaded", Toast.LENGTH_SHORT).show()
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

    override fun filter(categoryId: Int?, year:Int?, month:Int?, day:Int?) {

        events.clear()
        if (categoryId == null){
            events.addAll(unfilteredEvents)
        }
        else {
            unfilteredEvents.map {
                if (it.categoryId == categoryId) {
                    events.add(it)
                }
            }
        }
        if (year != null) {
            val yearFilteredEvents = events.filter { TimeFormat.getNumericalYear(it.startDateTime) == year }
            events.clear()
            events.addAll(yearFilteredEvents)
            if (month != null) {
                val monthFilteredEvents = events.filter { TimeFormat.getNumericalMonth(it.startDateTime) == month }
                events.clear()
                events.addAll(monthFilteredEvents)
                if (day != null) {
                    val dayFilteredEvents = events.filter { TimeFormat.getNumericalDay(it.startDateTime) == day }
                    events.clear()
                    events.addAll(dayFilteredEvents)
                }
            }
        }

        eventAdapter.notifyDataSetChanged()
        mapFragment?.onDataPassed(events)
        drawer_layout.closeDrawer(Gravity.START)
        if (categoryId != null || year != null)
            showFilteredEventsView(true)
    }

    override fun reset() {
        events.clear()
        events.addAll(unfilteredEvents)
        eventAdapter.notifyDataSetChanged()
        mapFragment?.onDataPassed(events)
        drawer_layout.closeDrawer(Gravity.START)
        showFilteredEventsView(false)
    }

    interface OnDataPassedListener {
        fun onDataPassed(events:MutableList<Event>)
    }
}
