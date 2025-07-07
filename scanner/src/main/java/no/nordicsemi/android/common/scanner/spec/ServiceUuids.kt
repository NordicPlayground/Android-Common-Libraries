/*
 * Copyright (c) 2025, Nordic Semiconductor
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

@file:OptIn(ExperimentalUuidApi::class)

package no.nordicsemi.android.common.scanner.spec

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.BatteryStd
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import no.nordicsemi.android.common.scanner.R
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlin.uuid.toKotlinUuid

internal val HTS_SERVICE_UUID= Uuid.parse("00001809-0000-1000-8000-00805f9b34fb")
internal val BPS_SERVICE_UUID= Uuid.parse("00001810-0000-1000-8000-00805f9b34fb")
internal val CSC_SERVICE_UUID= Uuid.parse("00001816-0000-1000-8000-00805f9b34fb")
internal val CGMS_SERVICE_UUID= Uuid.parse("0000181F-0000-1000-8000-00805f9b34fb")
internal val DF_SERVICE_UUID= Uuid.parse("21490000-494a-4573-98af-f126af76f490")
internal val GLS_SERVICE_UUID= Uuid.parse("00001808-0000-1000-8000-00805f9b34fb")
internal val HRS_SERVICE_UUID= Uuid.parse("0000180D-0000-1000-8000-00805f9b34fb")
internal val PRX_SERVICE_UUID= Uuid.parse("00001802-0000-1000-8000-00805f9b34fb")
internal val RSCS_SERVICE_UUID= Uuid.parse("00001814-0000-1000-8000-00805F9B34FB")
internal val UART_SERVICE_UUID= Uuid.parse("6E400001-B5A3-F393-E0A9-E50E24DCCA9E")
internal val BATTERY_SERVICE_UUID= Uuid.parse("0000180F-0000-1000-8000-00805f9b34fb")
internal val THROUGHPUT_SERVICE_UUID = Uuid.parse("0483DADD-6C9D-6CA9-5D41-03AD4FFF4ABB")
internal val CHANNEL_SOUND_SERVICE_UUID = Uuid.parse("0000185B-0000-1000-8000-00805F9B34FB")
internal val MDS_SERVICE_UUID = Uuid.parse("54220000-f6a5-4007-a371-722f4ebd8436")

// TODO: make a callback which checks the uuid and returns the name and icon for the service.
object ServiceUuids {

    @Composable
    @OptIn(ExperimentalUuidApi::class)
    fun getServiceInfo(uuid: Uuid): ServiceNameWithIcon? {
        return when (uuid) {
            HTS_SERVICE_UUID -> ServiceNameWithIcon("Health Thermometer", Icons.Default.Thermostat)
            BPS_SERVICE_UUID -> ServiceNameWithIcon("Blood Pressure", Icons.Default.Bloodtype)
            CSC_SERVICE_UUID -> ServiceNameWithIcon(
                "Cycling Speed and Cadence",
                Icons.AutoMirrored.Default.DirectionsBike,
            )

            CGMS_SERVICE_UUID -> ServiceNameWithIcon(
                "Continuous Glucose Monitoring",
                Icons.Outlined.WaterDrop
            )

            DF_SERVICE_UUID -> ServiceNameWithIcon("Direction Finding", Icons.Default.MyLocation)
            GLS_SERVICE_UUID -> ServiceNameWithIcon("Glucose", Icons.Outlined.WaterDrop)
            HRS_SERVICE_UUID -> ServiceNameWithIcon("Heart Rate", Icons.Default.MonitorHeart)
            PRX_SERVICE_UUID -> ServiceNameWithIcon("Proximity", Icons.Default.Thermostat)
            RSCS_SERVICE_UUID -> ServiceNameWithIcon(
                "Running Speed and Cadence",
                Icons.AutoMirrored.Default.DirectionsRun
            )

            UART_SERVICE_UUID -> ServiceNameWithIcon("UART", Icons.Default.SwapHoriz)
            BATTERY_SERVICE_UUID -> ServiceNameWithIcon("Battery", Icons.Default.BatteryStd)
            THROUGHPUT_SERVICE_UUID -> ServiceNameWithIcon("Throughput", Icons.Default.SyncAlt)
            CHANNEL_SOUND_SERVICE_UUID -> ServiceNameWithIcon(
                "Channel Sounding",
                Icons.Default.MyLocation
            )
            MDS_SERVICE_UUID -> ServiceNameWithIcon(
                "Memfault",
                ImageVector.vectorResource(R.drawable.ic_memfault_app_logo)
            )

            else -> null
        }
    }
}

data class ServiceNameWithIcon(
    val service: String,
    val icon: ImageVector,
)