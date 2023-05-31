package no.nordicsemi.android.common.core

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build

const val GOOGLE_PLAY_LINK = "https://play.google.com/store/apps/details?id="
object AppLauncher {
    /**
     * Opens the app with supplied package name, or opens Google Play if the app is not installed.
     */
    fun lunch(
        packageManager: PackageManager,
        packageName: String,
        context: Context
    ) {
        when (isPackageInstalled(packageName, packageManager)) {
            true -> openApp(packageManager, packageName, context)
            false -> try {
                openGooglePlay(packageName, context)
            } catch (e: ActivityNotFoundException) {
                // An Exception will be thrown if the Play Store is not installed on the target device.
                e.printStackTrace()
            }
        }
    }

    private fun openGooglePlay(packageName: String, context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_LINK + packageName))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    private fun openApp(
        packageManager: PackageManager,
        packageName: String,
        context: Context
    ) {
        val launchIntent: Intent? = packageManager.getLaunchIntentForPackage(packageName)
        launchIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        launchIntent?.let { context.startActivity(it) }
    }

    private fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfoCompat(packageName)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            false
        }
    }

    private fun PackageManager.getPackageInfoCompat(
        packageName: String,
        flags: Int = 0
    ): PackageInfo {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
        } else {
            @Suppress("DEPRECATION")
            getPackageInfo(packageName, flags)
        }
    }
}