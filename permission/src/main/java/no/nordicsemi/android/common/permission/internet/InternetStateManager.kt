package no.nordicsemi.android.common.permission.internet

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import no.nordicsemi.android.common.permission.util.NotAvailable
import no.nordicsemi.android.common.permission.util.Available
import no.nordicsemi.android.common.permission.util.FeatureNotAvailableReason
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class InternetStateManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun networkState() = callbackFlow {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                trySend(Available)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                val isAvailable = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
                trySend(if (isAvailable) Available else NotAvailable(FeatureNotAvailableReason.DISABLED))
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                trySend(NotAvailable(FeatureNotAvailableReason.DISABLED))
            }
        }

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }
}
