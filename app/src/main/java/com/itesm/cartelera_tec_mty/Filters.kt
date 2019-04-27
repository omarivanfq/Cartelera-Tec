package com.itesm.cartelera_tec_mty

import NetworkUtility.NetworkConnection
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.fragment_filters.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import java.time.Month

class Filters : Fragment() {

    // TODO: Rename and change types of parameters
  //  private var mParam1: String? = null
    // private var mParam2: String? = null
  //  private var mListener: OnFragmentInteractionListener? = null

    private var category:Int? = null
    private var year:Int? = null
    private var month:Int? = null
    private var day:Int? = null

    private lateinit var daysAdapter:ArrayAdapter<String>
    private lateinit var categoriesAdapter:ArrayAdapter<String>
    private var days:MutableList<String> = mutableListOf()
    private val longMonths:ArrayList<Int> = arrayListOf(1, 3, 5, 7, 8, 10, 12) // iniciando en 1
    private var categories:ArrayList<Category> = arrayListOf()
    lateinit var filteringListener:FilteringListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_filters, container, false)
        setYearsSpinner(view)
        setMonthsSpinner(view)
        setDaysSpinner(view)
        setCategorySpinner(view)
        loadCategories()

        val textviewDate = view.findViewById<TextView>(R.id.textview_date)
        val containerDate = view.findViewById<LinearLayout>(R.id.container_date_filter)
        val layoutDate = view.findViewById<LinearLayout>(R.id.layout_date_filter)
        containerDate.removeView(layoutDate)

        textviewDate.setOnClickListener {
            if (containerDate == layoutDate.parent) {
                containerDate.removeView(layoutDate)
            }
            else {
                containerDate.addView(layoutDate)
            }
        }

        val textviewCategory = view.findViewById<TextView>(R.id.textview_category)
        val containerCategory = view.findViewById<LinearLayout>(R.id.container_category_filter)
        val layoutCategory = view.findViewById<LinearLayout>(R.id.layout_category_filter)
        containerCategory.removeView(layoutCategory)
        textviewCategory.setOnClickListener {
            if (containerCategory == layoutCategory.parent) {
                containerCategory.removeView(layoutCategory)
            }
            else {
                containerCategory.addView(layoutCategory)
            }
        }

        val btnFilterCategory:Button = view.findViewById(R.id.btn_filters_done)
        btnFilterCategory.setOnClickListener {
            filteringListener.filter(category, year, month, day)
        }
        val btnRemoveFilter:Button = view.findViewById(R.id.btn_remove_filters)
        btnRemoveFilter.setOnClickListener {
            if (containerDate == layoutDate.parent) {
                val spinnerYear:Spinner = view.findViewById(R.id.spinner_years)
                val spinnerMonth:Spinner = view.findViewById(R.id.spinner_months)
                val spinnerDay:Spinner = view.findViewById(R.id.spinner_days)
                spinnerYear.setSelection(0)
                spinnerMonth.setSelection(0)
                spinnerDay.setSelection(0)
            }
            if (containerCategory == layoutCategory.parent) {
                val spinnerCategory: Spinner = view.findViewById(R.id.spinner_category)
                spinnerCategory.setSelection(0)
            }
            filteringListener.reset()
        }

        return view
    }

    private fun loadCategories() {
        if (NetworkConnection.isNetworkConnected(activity)){
            doAsync {
                val url = NetworkConnection.buildCategoriesUrl()
                val dataJson = NetworkConnection.getResponseFromHttpUrl(url)
                uiThread {
                    val jsonArray = JSONArray(dataJson)
                    var i = 0
                    while (i < jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        categories.add(
                            Category(
                                jsonObject.getInt("id"),
                                jsonObject.getString("name")
                            )
                        )
                        i++
                        updateCategories()
                    }
                }
            }
        }
    }

    private fun setCategorySpinner(vieww:View) {
        val spinnerCategory = vieww.findViewById<Spinner>(R.id.spinner_category)
        val categoriesNames:MutableList<String> = mutableListOf()
        categoriesAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, categoriesNames)
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = categoriesAdapter
        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                category = if (position == 0) {
                    null
                } else {
                    categories[position - 1].id
                }
            }
        }
    }

    private fun updateCategories() {
        categoriesAdapter.clear()
        val categoriesNames:MutableList<String> = mutableListOf("Todas")
        categoriesNames.addAll(categories.map { it.name })
        categoriesAdapter.addAll(categoriesNames)
        categoriesAdapter.notifyDataSetChanged()
    }

    private fun setYearsSpinner(view:View) {
        val spinnerYears = view.findViewById<Spinner>(R.id.spinner_years)
        val yearsListInt:List<Int> = (2019..2022).toList()
        val yearsListString:ArrayList<String> = arrayListOf("Año")
        yearsListString.addAll(yearsListInt.map {it.toString()})
        val dataAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, yearsListString)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerYears.adapter = dataAdapter
        spinnerYears.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0){
                    spinner_months.isEnabled = false
                    spinner_months.setSelection(0)
                    spinner_days.isEnabled = false
                    spinner_days.setSelection(0)
                    year = null
                }
                else {
                    year = yearsListInt[position - 1]
                    spinner_months.isEnabled = true
                }
            }
        }
    }

    private fun setMonthsSpinner(view:View) {
        val spinnerMonths = view.findViewById<Spinner>(R.id.spinner_months)
        val adapterMonths = ArrayAdapter.createFromResource(activity, R.array.months_array, android.R.layout.simple_spinner_item)
        adapterMonths.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMonths.adapter = adapterMonths
       // spinnerMonths.isEnabled = false
        spinnerMonths.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
           override fun onNothingSelected(parent: AdapterView<*>?) { }
           override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
               if (position == 0){
                   spinner_days.isEnabled = false
                   spinner_days.setSelection(0)
                   month = null
               }
               else {
                   if (position == 2) {
                       if (isLeapYear(spinner_years.selectedItem.toString().toInt())) {
                           updateDaysAdapter(29)
                       } else {
                           updateDaysAdapter(28)
                       }
                   } else if (position in longMonths) {
                       updateDaysAdapter(31)
                   } else {
                       updateDaysAdapter(30)
                   }
                   spinner_days.isEnabled = true
                   month = position
               }
           }
       }
        spinnerMonths.isEnabled = false
    }

    private fun setDaysSpinner(view:View) {
        val spinnerDays = view.findViewById<Spinner>(R.id.spinner_days)
        days.add("Dia")
        daysAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, days)
        daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDays.adapter = daysAdapter
        spinnerDays.isEnabled = false
        spinnerDays.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                day = if (position == 0) {
                    null
                } else {
                    position
                }
            }
        }
    }

    private fun updateDaysAdapter(daysMax:Int) {
        days.clear()
        days.add("Dia")
        days.addAll((1..daysMax).map {it.toString()})
        daysAdapter.notifyDataSetChanged()
    }
    private fun isLeapYear(year:Int):Boolean = ((year % 4 == 0) && (year % 100!= 0)) || (year % 400 == 0)

    interface FilteringListener {
        fun filter(categoryId: Int?, year:Int?, month:Int?, day:Int?)
        fun reset()
    }

/*
    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }
    */

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is FilteringListener) {
            filteringListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement FilteringListener")
        }
    }

/*
    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
    */
/*
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }
*/
    /*
    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Filters.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): Filters {
            val fragment = Filters()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*    if (arguments != null) {
                mParam1 = arguments.getString(ARG_PARAM1)
                mParam2 = arguments.getString(ARG_PARAM2)
            }*/
    }

}// Required empty public constructor

