/*
 * Copyright (c) 2023, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list
 * of conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package no.nordicsemi.android.common.theme

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat

/**
 * Base activity that sets the Nordic theme and the Splash Screen.
 *
 * The Activity must be declared using NordicTheme.SplashScreen theme in the manifest.
 * The theme will be changed to NordicTheme when the splash screen animation is complete.
 */
abstract class NordicActivity : ComponentActivity() {

    companion object {
        private var coldStart = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.NordicTheme)
        super.onCreate(savedInstanceState)

        setDecorFitsSystemWindows(true)

        val view = window.decorView
        if (!isDarkMode()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                )
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val flags = view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                view.systemUiVisibility = flags
            }
        }

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

    fun setDecorFitsSystemWindows(decorFitsSystemWindows: Boolean) {
        if (!decorFitsSystemWindows) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        } else {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusBarColor)
        }
    }

    fun isDarkMode(): Boolean {
        val darkModeFlag = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return darkModeFlag == Configuration.UI_MODE_NIGHT_YES
    }
}
