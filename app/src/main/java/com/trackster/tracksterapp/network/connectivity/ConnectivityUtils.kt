
import android.content.Context
import android.net.ConnectivityManager

object ConnectivityUtils {

    /**
     * Checks if there is a network connection.
     *
     * @return true if network connectivity exists, false otherwise.
     */
    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}