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

package no.nordicsemi.android.common.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource

val ColorScheme.nordicBlue
    @Composable
    get() = colorResource(id = R.color.nordicBlue)

val ColorScheme.nordicSky
    @Composable
    get() = colorResource(id = R.color.nordicSky)

val ColorScheme.nordicBlueslate
    @Composable
    get() = colorResource(id = R.color.nordicBlueslate)

val ColorScheme.nordicLake
    @Composable
    get() = colorResource(id = R.color.nordicLake)

val ColorScheme.nordicGrass
    @Composable
    get() = colorResource(id = R.color.nordicGrass)

val ColorScheme.nordicSun
    @Composable
    get() = colorResource(id = R.color.nordicSun)

val ColorScheme.nordicRed
    @Composable
    get() = colorResource(id = R.color.nordicRed)

val ColorScheme.nordicFall
    @Composable
    get() = colorResource(id = R.color.nordicFall)

val ColorScheme.nordicLightGray
    @Composable
    get() = colorResource(id = R.color.nordicLightGray)

val ColorScheme.nordicMiddleGray
    @Composable
    get() = colorResource(id = R.color.nordicMiddleGray)

val ColorScheme.nordicDarkGray
    @Composable
    get() = colorResource(id = R.color.nordicDarkGray)
