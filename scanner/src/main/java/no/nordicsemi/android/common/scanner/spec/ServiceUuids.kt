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

package no.nordicsemi.android.common.scanner.spec

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryStd
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.SocialDistance
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.ui.graphics.vector.ImageVector
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlin.uuid.toKotlinUuid

internal val HTS_SERVICE_UUID = UUID.fromString("00001809-0000-1000-8000-00805f9b34fb")
internal val BPS_SERVICE_UUID: UUID = UUID.fromString("00001810-0000-1000-8000-00805f9b34fb")
internal val CSC_SERVICE_UUID: UUID = UUID.fromString("00001816-0000-1000-8000-00805f9b34fb")
internal val CGMS_SERVICE_UUID: UUID = UUID.fromString("0000181F-0000-1000-8000-00805f9b34fb")
internal val DF_SERVICE_UUID: UUID = UUID.fromString("21490000-494a-4573-98af-f126af76f490")
internal val GLS_SERVICE_UUID: UUID = UUID.fromString("00001808-0000-1000-8000-00805f9b34fb")
internal val HRS_SERVICE_UUID: UUID = UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb")
internal val PRX_SERVICE_UUID: UUID = UUID.fromString("00001802-0000-1000-8000-00805f9b34fb")
internal val RSCS_SERVICE_UUID: UUID = UUID.fromString("00001814-0000-1000-8000-00805F9B34FB")
internal val UART_SERVICE_UUID: UUID = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E")
internal val BATTERY_SERVICE_UUID: UUID = UUID.fromString("0000180F-0000-1000-8000-00805f9b34fb")
internal val THROUGHPUT_SERVICE_UUID: UUID = UUID.fromString("0483DADD-6C9D-6CA9-5D41-03AD4FFF4ABB")
internal val CHANNEL_SOUND_SERVICE_UUID: UUID =
    UUID.fromString("0000185B-0000-1000-8000-00805F9B34FB")

// TODO: make a callback which checks the uuid and returns the name and icon for the service.
object ServiceUuids {

    @OptIn(ExperimentalUuidApi::class)
    fun getServiceInfo(uuid: Uuid): ServiceNameWithIcon? {
        return when (uuid) {
            HTS_SERVICE_UUID.toKotlinUuid() -> ServiceNameWithIcon("Health Thermometer", Icons.Default.Thermostat)
            BPS_SERVICE_UUID.toKotlinUuid() -> ServiceNameWithIcon("Blood Pressure", Icons.Default.Bloodtype)
            CSC_SERVICE_UUID.toKotlinUuid() -> ServiceNameWithIcon(
                "Cycling Speed and Cadence",
                Icons.Default.Thermostat
            )

            CGMS_SERVICE_UUID.toKotlinUuid() -> ServiceNameWithIcon(
                "Continuous Glucose Monitoring",
                Icons.Default.Thermostat
            )

            DF_SERVICE_UUID.toKotlinUuid() -> ServiceNameWithIcon("DFU", Icons.Default.Thermostat)
            GLS_SERVICE_UUID.toKotlinUuid() -> ServiceNameWithIcon("Glucose", Icons.Default.Thermostat)
            HRS_SERVICE_UUID.toKotlinUuid() -> ServiceNameWithIcon("Heart Rate", Icons.Default.Thermostat)
            PRX_SERVICE_UUID.toKotlinUuid() -> ServiceNameWithIcon("Proximity", Icons.Default.Thermostat)
            RSCS_SERVICE_UUID.toKotlinUuid() -> ServiceNameWithIcon(
                "Running Speed and Cadence",
                Icons.Default.Thermostat
            )

            UART_SERVICE_UUID.toKotlinUuid() -> ServiceNameWithIcon("UART", Icons.Default.Thermostat)
            BATTERY_SERVICE_UUID.toKotlinUuid() -> ServiceNameWithIcon("Battery", Icons.Default.BatteryStd)
            THROUGHPUT_SERVICE_UUID.toKotlinUuid() -> ServiceNameWithIcon("Throughput", Icons.Default.SyncAlt)
            CHANNEL_SOUND_SERVICE_UUID.toKotlinUuid() -> ServiceNameWithIcon(
                "Channel Sounding",
                Icons.Default.SocialDistance
            )

            else -> null
        }
    }
}

data class ServiceNameWithIcon(
    val service: String,
    val icon: ImageVector,
)