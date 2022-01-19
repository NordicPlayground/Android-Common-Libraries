package no.nordicsemi.android.material.you

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

abstract class NordicActivity : ComponentActivity() {

    private var coldStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.appBarColor)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val splashScreen = installSplashScreen()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (coldStart) {
                    coldStart = false
                    val then = System.currentTimeMillis()
                    splashScreen.setKeepOnScreenCondition {
                        val now = System.currentTimeMillis()
                        now < then + 900
                    }
                }
            }
        }
    }
}
