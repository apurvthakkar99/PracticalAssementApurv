package com.example.scotiapracticaltest.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.text.SimpleDateFormat
import java.util.Locale

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
    return networkCapabilities != null && (
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            )

}

fun formatDate(updatedAt: String): String {
    val inputFormat = SimpleDateFormat(AppConstants.DATEFORMATYYYYMMDD, Locale.getDefault())
    val outputFormat = SimpleDateFormat(AppConstants.DATEFORMATMMMDDYYYY, Locale.getDefault())

    return try {
        val date = inputFormat.parse(updatedAt)
        outputFormat.format(date ?: return "Invalid date")
    } catch (e: Exception) {
        "Invalid date"
    }
}



