package NetworkUtility

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class NetworkConnection {

    companion object {
        const val BASE_URL = "https://cartelerai-api.herokuapp.com"
        lateinit var data: InputStream
        lateinit var dataStr: String
        fun buildEventsUrl(): URL =
                URL("$BASE_URL/events")

        fun buildSponsorsUrl(): URL =
                URL("$BASE_URL/sponsors")

        fun getResponseFromHttpUrl(url: URL): String {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("Accept", "application/vnd.cartelera-api.v1")
                connection.setRequestProperty("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxfQ.sgAjfo5zm77syDfPFxnMzt6ASC2JcAhGdKb8jCJCBBs")
                connection.connect()
                val inputStreamReader = InputStreamReader(connection.inputStream)
                val rd = BufferedReader(inputStreamReader)
                return rd.readLine()
            } catch (e: IOException) {
                e.printStackTrace()
                throw IOException("Not connected")
            }
        }

        fun isNetworkConnected(context:Context): Boolean {
            val connectivityManager: ConnectivityManager = context.getSystemService(
                    Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            return networkInfo?.isConnectedOrConnecting ?: false
        }

    }

}



