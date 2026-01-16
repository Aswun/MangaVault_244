package com.example.mangavault.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * Utility object untuk mengecek status koneksi jaringan.
 */
object NetworkUtils {

    /**
     * Memeriksa apakah perangkat terhubung ke internet.
     * Mendukung pemeriksaan WiFi, Cellular Data, dan Ethernet.
     *
     * @param context Context aplikasi.
     * @return Boolean true jika terhubung internet, false jika tidak.
     */
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}