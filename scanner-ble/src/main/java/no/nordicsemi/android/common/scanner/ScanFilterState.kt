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

package no.nordicsemi.android.common.scanner

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import no.nordicsemi.android.common.scanner.data.Filter
import no.nordicsemi.android.common.scanner.data.OnlyNearby
import no.nordicsemi.android.common.scanner.data.OnlyWithNames
import no.nordicsemi.android.common.scanner.data.SortByName
import no.nordicsemi.android.common.scanner.data.SortByRssi
import no.nordicsemi.android.common.scanner.data.SortingDisabled
import no.nordicsemi.android.common.scanner.data.SortingOption
import no.nordicsemi.kotlin.ble.client.android.ConjunctionFilterScope

/**
 * Represents the state of the scanner, including available filters and sorting options.
 *
 * This interface provides methods to check if a filter is selected and to retrieve the active
 * sorting comparator based on the currently selected sorting option.
 */
@Stable
interface ScanFilterState {

    /**
     * The filter to be applied to all scan results.
     */
    val filter: ConjunctionFilterScope.() -> Unit

    /**
     * The list of filters that can be applied to the scanner results.
     */
    val dynamicFilters: List<Filter>

    /**
     * The list of sorting options that can be applied to the results.
     *
     * If the list is not empty, an option to clear the sorting will be added.
     * An empty list will hide the sorting options in the UI.
     */
    val sortingOptions: List<SortingOption>

    /**
     * The active sorting option.
     *
     * If no sorting option is selected, this will return [SortingDisabled].
     */
    val activeSortingOption: SortingOption

    /**
     * A flag indicating whether any dynamic filters or sorting options are selected.
     */
    val isEmpty: Boolean

    /**
     * Checks if the filter at the given index is selected.
     *
     * @param index The index of the filter to check.
     * @return `true` if the filter is selected, `false` otherwise.
     */
    fun isFilterSelected(index: Int): Boolean

    /**
     * Toggles the selection state of the filter at the given index.
     *
     * If the filter is currently selected, it will be deselected, and vice versa.
     *
     * @param index The index of the filter to toggle.
     */
    fun toggleFilter(index: Int)

    /**
     * Sets the active sorting option.
     *
     * If the provided sorting option is not in the list of available sorting options, it will be ignored.
     *
     * @param sortingOption The sorting option to set as active.
     */
    fun sortBy(sortingOption: SortingOption)

    /**
     * Clears all selected filters and sorting options.
     *
     * This will reset the state to its initial empty state.
     */
    fun clear()
}

/**
 * Creates a [ScanFilterState] that can be used to manage the state of the scanner.
 *
 * @param filter A static filter to be applied to all scan results. Defaults to an empty filter.
 * @param dynamicFilters The list of filters to be shown in the scanner. Defaults to a list containing
 * [OnlyNearby], [OnlyWithNames].
 * @param sortingOptions The list of sorting options to be shown in the scanner. Defaults to a list
 * containing [SortByRssi] and [SortByName]. If the list is not empty, an option to clear the
 * sorting will be added.
 */
@Composable
fun rememberFilterState(
    filter: ConjunctionFilterScope.() -> Unit = {},
    dynamicFilters: List<Filter> = listOf(
        OnlyNearby(),
        OnlyWithNames(isInitiallySelected = true),
    ),
    sortingOptions: List<SortingOption> = listOf(
        SortByRssi(),
        SortByName()
    ),
): ScanFilterState {
    val initiallySelected = dynamicFilters.mapIndexedNotNull { i, f -> if (f.isInitiallySelected) i else null }.toSet()
    var selectedFilters by rememberSaveable { mutableStateOf(initiallySelected) }
    var selectedSorting by rememberSaveable { mutableStateOf<Int?>(null) }

    return object : ScanFilterState {
        override val filter: ConjunctionFilterScope.() -> Unit = filter
        override val dynamicFilters: List<Filter> = dynamicFilters
        override val sortingOptions: List<SortingOption> = sortingOptions

        override val activeSortingOption: SortingOption
            get() = selectedSorting?.let { sortingOptions[it] } ?: SortingDisabled

        override val isEmpty: Boolean
            get() = selectedFilters.isEmpty() && selectedSorting == null

        override fun isFilterSelected(index: Int) = selectedFilters.contains(index)

        override fun toggleFilter(index: Int) {
            selectedFilters = if (selectedFilters.contains(index)) {
                selectedFilters - index
            } else {
                selectedFilters + index
            }
        }

        override fun sortBy(sortingOption: SortingOption) {
            selectedSorting = sortingOptions.indexOf(sortingOption).takeIf { it >= 0 }
        }

        override fun clear() {
            selectedFilters = emptySet()
            selectedSorting = null
        }
//
//        override fun equals(other: Any?): Boolean {
//            val e = other is ScanFilterState &&
//                other.activeSortingOption == activeSortingOption &&
//                other.isEmpty == isEmpty &&
//                dynamicFilters.foldIndexed(true) { index, acc, _ ->
//                    acc && (other.isFilterSelected(index) == isFilterSelected(index))
//                }
//            Log.i("AAA", "ScanFilterState#equals: $e")
//            return e
//        }
    }
}