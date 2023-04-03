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

@Composable
fun ColorScheme.nordicBlue() = colorResource(id = R.color.nordicBlue)

@Composable
fun ColorScheme.nordicSky() = colorResource(id = R.color.nordicSky)

@Composable
fun ColorScheme.nordicBlueslate() = colorResource(id = R.color.nordicBlueslate)

@Composable
fun ColorScheme.nordicLake() = colorResource(id = R.color.nordicLake)

@Composable
fun ColorScheme.nordicGrass() = colorResource(id = R.color.nordicGrass)

@Composable
fun ColorScheme.nordicSun() = colorResource(id = R.color.nordicSun)

@Composable
fun ColorScheme.nordicRed() = colorResource(id = R.color.nordicRed)

@Composable
fun ColorScheme.nordicFall() = colorResource(id = R.color.nordicFall)

@Composable
fun ColorScheme.nordicLightGray() = colorResource(id = R.color.nordicLightGray)

@Composable
fun ColorScheme.nordicMiddleGray() = colorResource(id = R.color.nordicMiddleGray)

@Composable
fun ColorScheme.nordicDarkGray() = colorResource(id = R.color.nordicDarkGray)
