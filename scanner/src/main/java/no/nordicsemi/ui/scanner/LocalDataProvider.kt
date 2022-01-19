package no.nordicsemi.ui.scanner

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.location.LocationManager
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat
import kotlinx.coroutines.flow.MutableStateFlow

private const val SHARED_PREFS_NAME = "SHARED_PREFS_NAME"
private const val PREFS_FILTER_UUID_REQUIRED = "filter_uuid"
private const val PREFS_FILTER_NEARBY_ONLY = "filter_nearby"

private const val PREFS_LOCATION_REQUIRED = "location_required"
private const val PREFS_PERMISSION_REQUESTED = "permission_requested"
private const val PREFS_BLUETOOTH_PERMISSION_REQUESTED = "bluetooth_permission_requested"

@SuppressLint("AnnotateVersionCheck")
class LocalDataProvider(private val context: Context) {

    val locationState = MutableStateFlow(isLocationRequiredAndEnabled())

    private val sharedPrefs: SharedPreferences
        get() = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    /**
     * The first time an app requests a permission there is no 'Don't ask again' checkbox and
     * [ActivityCompat.shouldShowRequestPermissionRationale] returns false.
     * This situation is similar to a permission being denied forever, so to distinguish both cases
     * a flag needs to be saved.
     *
     * @param context the context.
     */
    var locationPermissionRequested: Boolean
        get() = sharedPrefs.getBoolean(PREFS_PERMISSION_REQUESTED, false)
        set(value) {
            sharedPrefs.edit().putBoolean(PREFS_PERMISSION_REQUESTED, value).apply()
        }

    /**
     * The first time an app requests a permission there is no 'Don't ask again' checkbox and
     * [ActivityCompat.shouldShowRequestPermissionRationale] returns false.
     * This situation is similar to a permission being denied forever, so to distinguish both cases
     * a flag needs to be saved.
     *
     * @param context the context.
     */
    var bluetoothPermissionRequested: Boolean
        get() = sharedPrefs.getBoolean(PREFS_BLUETOOTH_PERMISSION_REQUESTED, false)
        set(value) {
            sharedPrefs.edit().putBoolean(PREFS_BLUETOOTH_PERMISSION_REQUESTED, value).apply()
        }

    var isLocationPermissionRequired: Boolean
        /**
         * Location enabled is required on some phones running Android 6 - 11
         * (for example on Nexus and Pixel devices). Initially, Samsung phones didn't require it,
         * but that has been fixed for those phones in Android 9.
         *
         * @param context the context.
         * @return False if it is known that location is not required, true otherwise.
         */
        get() = sharedPrefs.getBoolean(PREFS_LOCATION_REQUIRED, isMarshmallowOrAbove && !isSOrAbove)
        /**
         * When a Bluetooth LE packet is received while Location is disabled it means that Location
         * is not required on this device in order to scan for LE devices. This is a case of Samsung
         * phones, for example. Save this information for the future to keep the Location info hidden.
         *
         * @param context the context.
         */
        set(value) {
            sharedPrefs.edit().putBoolean(PREFS_LOCATION_REQUIRED, value).apply()
        }

    val isMarshmallowOrAbove: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    val isSOrAbove: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    private fun isLocationRequiredAndEnabled(): Boolean {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return isLocationPermissionRequired && !LocationManagerCompat.isLocationEnabled(lm)
    }

    fun refreshLocationState() {
        locationState.value = isLocationRequiredAndEnabled()
    }
}
