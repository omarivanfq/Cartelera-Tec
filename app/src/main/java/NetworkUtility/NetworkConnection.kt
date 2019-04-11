package NetworkUtility

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import java.io.IOException
import java.net.URL

class NetworkConnection {

    companion object {
        const val BASE_URL = "https://cartelerai-api.herokuapp.com"

        fun buildEventsUrl(): URL =
                URL("${BASE_URL}/events")

        fun buildSponsorsUrl(): URL =
                URL("${BASE_URL}/sponsors")

        fun getResponseFromHttpUrl(url: URL): String =
                try {
                    url.readText()
                } catch (e: IOException) {
                    e.printStackTrace()
                    throw IOException("Not connected")
                }

        fun isNetworkConnected(context:Context): Boolean {
            val connectivityManager: ConnectivityManager = context.getSystemService(
                    Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            return networkInfo?.isConnectedOrConnecting ?: false
        }

    }

}



