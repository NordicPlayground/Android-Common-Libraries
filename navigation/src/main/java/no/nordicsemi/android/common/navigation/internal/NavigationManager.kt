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

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import no.nordicsemi.android.common.navigation.DestinationId
import no.nordicsemi.android.common.navigation.NavigationResult
import no.nordicsemi.android.common.navigation.Navigator
import javax.inject.Inject

/**
 * A navigation manager that can be used to navigate to next destination, or back.
 *
 * @param context the application context.
 * @property executor The [NavigationExecutor] that will perform the navigation.
 * @property savedStateHandle The [SavedStateHandle] that will be used to store the navigation
 * result.
 */
@ActivityRetainedScoped
internal class NavigationManager @Inject constructor(
    @ApplicationContext private val context: Context,
): Navigator {
    internal var executor: NavigationExecutor? = null
    internal var savedStateHandle: SavedStateHandle? = null

    override fun <R> resultFrom(from: DestinationId<*, R>): Flow<NavigationResult<R>> =
        @Suppress("UNCHECKED_CAST")
        savedStateHandle?.run {
            getStateFlow<NavigationResultState>(from.name, Initial)
                .transform { result ->
                    when (result) {
                        // Ignore the initial value.
                        is Initial -> {}
                        // Return success result.
                        is Success<*> -> emit(NavigationResult.Success(result.value as R))
                        // Return null when cancelled.
                        is Cancelled -> emit(NavigationResult.Cancelled())
                    }
                }
        } ?: throw IllegalStateException("SavedStateHandle is not set")

    override fun <A> navigateTo(to: DestinationId<A, *>, args: A) {
        executor?.navigate(NavigationTarget(to, args))
    }

    override fun <R> navigateUpWithResult(from: DestinationId<*, R>, result: R) {
        executor?.navigateUpWithResult(Success(result))
    }

    override fun navigateUp() {
        executor?.navigateUpWithResult(Cancelled)
    }

    override fun open(link: Uri) {
        try {
            with (Intent(Intent.ACTION_VIEW, link)) {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(this)
            }
        } catch (e: Exception) {
            Log.e("Navigator", "Failed to open link: $link", e)
        }
    }
}