package com.itesm.cartelera_tec_mty

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Context

class FavoritesTab : Fragment() {

    lateinit var adapter:EventAdapter
    lateinit var listIds:ArrayList<Int>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.favorites_tab, container, false)

        return rootView
    }

    fun setArrayPrefs(arrayName: String, array: ArrayList<Int>, mContext: Context) {
        val prefs = mContext.getSharedPreferences(SP_NAME, 0)
        val editor = prefs.edit()
        editor.putInt(arrayName + "_size", array.size)
        for (i in 0 until array.size)
            editor.putInt(arrayName + "_" + i, array[i])
        editor.apply()
    }

    fun deleteArrayPrefs(arrayName: String, array: ArrayList<Int>, mContext: Context){
        val prefs = mContext.getSharedPreferences(SP_NAME, 0)
        val editor = prefs.edit()
        editor.putInt(arrayName + "_size", 0)
        for (i in 0 until array.size)
            editor.remove(arrayName + "_" + i)
        editor.apply()
    }

    fun getArrayPrefs(arrayName: String, mContext: Context): ArrayList<Int> {
        val prefs = mContext.getSharedPreferences(SP_NAME, 0)
        val size = prefs.getInt(arrayName + "_size", 0)
        val array:ArrayList<Int> = arrayListOf()
        (0 until size).mapTo(array) { prefs.getInt(arrayName + "_" + it, 0) }
        return array
    }

    companion object {
        val SP_NAME = "sharedpreferences_favorites"
        val ARRAY_NAME = "listids"
    }
}