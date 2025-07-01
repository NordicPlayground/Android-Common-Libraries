package no.nordicsemi.android.common.scanner

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
    }
}