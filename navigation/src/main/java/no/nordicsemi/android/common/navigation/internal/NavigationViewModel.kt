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

package no.nordicsemi.android.common.navigation.internal

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class NavigationViewModel @Inject constructor(
    private val navigationManager: NavigationManager,
): ViewModel(), NavigationExecutor {
    /** The navigation events class. */
    sealed class Event {
        data class NavigateTo(val route: String, val args: Bundle?) : Event()
        data class NavigateUp(val result: Any?) : Event()
    }

    private val _events = MutableStateFlow<Event?>(null)
    val events = _events.asStateFlow()

    init {
        navigationManager.executor = this
    }

    /**
     * The given [SavedStateHandle] will be used to store the navigation result.
     *
     * This has to be the instance given in navigation composable, as each ViewModel may received
     * a different instance of [SavedStateHandle] using Hilt injections.
     */
    fun use(savedStateHandle: SavedStateHandle) = apply {
        navigationManager.savedStateHandle = savedStateHandle
    }

    /**
     * After the navigation is completed, this method should be called to consume the event.
     * Otherwise, it will be emitted again. This covers a case, when the event was received, but
     * the consumer was destroyed before it could handle it.
     */
    fun consumeEvent() {
        _events.update { null }
    }

    override fun <A> navigate(target: NavigationTarget<A>) {
        _events.update { Event.NavigateTo(target.destination.name, target.toBundle()) }
    }

    override fun navigateUpWithResult(result: NavigationResultState) {
        _events.update { Event.NavigateUp(result) }
    }

    override fun onCleared() {
        super.onCleared()
        navigationManager.executor = null
    }
}