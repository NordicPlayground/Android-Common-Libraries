/*
 * Copyright (c) 2022, Nordic Semiconductor
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

package no.nordicsemi.android.common.test.main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.union
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.navigation.createSimpleDestination
import no.nordicsemi.android.common.navigation.defineDestination
import no.nordicsemi.android.common.test.main.page.BasicsPage
import no.nordicsemi.android.common.test.main.page.FontsPage
import no.nordicsemi.android.common.test.main.page.WarningPage
import no.nordicsemi.android.common.test.main.page.WizardPage
import no.nordicsemi.android.common.test.simple.HelloDestinations
import no.nordicsemi.android.common.ui.view.PagerView
import no.nordicsemi.android.common.ui.view.PagerViewEntity

/** This is the destination identifier. */
val Main = createSimpleDestination("main")

/**
 * Here you define a View for the destination.
 */
private val MainDestination = defineDestination(Main) {
    MainScreen()
}

val MainDestinations = MainDestination + HelloDestinations

@Composable
private fun MainScreen() {
    val pages = PagerViewEntity(listOf(
        BasicsPage,
        FontsPage,
        WizardPage,
        WarningPage,
    ))
    // Pad content with cutout, at least 16dp on each side.
    val padding = WindowInsets.displayCutout
        .only(WindowInsetsSides.Horizontal)
        .union(WindowInsets(left = 16.dp, right = 16.dp))
        .asPaddingValues()
    // Use the greatest padding for the spacing, so that other pages aren't visible
    // being the cutout.
    val spacing = maxOf(
        padding.calculateLeftPadding(LayoutDirection.Ltr),
        padding.calculateRightPadding(LayoutDirection.Rtl)
    )
    PagerView(
        viewEntity = pages,
        contentPadding = padding,
        itemSpacing = spacing,
    )
}
