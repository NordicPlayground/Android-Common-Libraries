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

package no.nordicsemi.android.common.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val _navigationDestination = MutableStateFlow(ConsumableNavigationDestination(InitialDestination))
    internal val navigationDestination = _navigationDestination.asStateFlow()

    private val arguments = mutableMapOf<DestinationId, DestinationArgument>()
    private val results = mutableMapOf<DestinationId, DestinationResult>()

    val recentArgument = MutableSharedFlow<DestinationArgument>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val recentResult = MutableSharedFlow<DestinationResult>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    fun consumeLastEvent() {
        _navigationDestination.value = _navigationDestination.value.copy(isConsumed = true)
    }

    fun navigateUp() {
        postDestination(BackDestination)
    }

    fun navigateUp(destinationId: DestinationId, args: DestinationResult) {
        results[destinationId] = args
        recentResult.tryEmit(args)
        postDestination(BackDestination)
    }

    fun navigateTo(destination: DestinationId, args: Argument? = null) {
        args?.let {
            val destinationArgument = DestinationArgument(destination, args)
            arguments[destination] = destinationArgument
            recentArgument.tryEmit(destinationArgument)
        }
        postDestination(ForwardDestination(destination))
    }

    private fun postDestination(destination: NavigationDestination) {
        _navigationDestination.value = ConsumableNavigationDestination(destination)
    }

    fun getImmediateArgument(destinationId: DestinationId) = arguments[destinationId]?.argument

    fun openLink(link: String) {
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(browserIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
