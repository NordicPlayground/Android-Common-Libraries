@file:Suppress("unused")

package no.nordicsemi.android.common.scanner.data

import androidx.annotation.StringRes
import no.nordicsemi.android.common.scanner.R
import no.nordicsemi.android.common.scanner.viewmodel.ScannedPeripheral

sealed class SortingOption(
    @StringRes val title: Int,
    val comparator: Comparator<ScannedPeripheral>
)

/**
 * Sort results alphabetically by device name.
 * 
 * Devices are ordered based on their names in ascending order.
 */
class SortByName(
    @StringRes title: Int = R.string.sort_by_alphabetical,
) : SortingOption(
    title = title,
    comparator = Comparator { left, right ->
        val leftName = left.latestScanResult.advertisingData.name
        val rightName = right.latestScanResult.advertisingData.name

        when {
            leftName == null && rightName == null -> 0 // Both names are null
            leftName == null -> 1 // Only right has a name
            rightName == null -> -1 // Only left has a name
            else -> leftName.compareTo(rightName, ignoreCase = true)
        }
    }
)

class SortByRssi(
    @StringRes title: Int = R.string.sort_by_rssi,
) : SortingOption(
    title = title,
    comparator = Comparator { left, right ->
        // Sort by RSSI in descending order
        right.highestRssi.compareTo(left.highestRssi)
    }
)

class CustomSorting(
    @StringRes title: Int,
    comparator: Comparator<ScannedPeripheral>
) : SortingOption(
    title = title,
    comparator = comparator
)

data object SortingDisabled : SortingOption(
    title = R.string.sort_by_none,
    comparator = Comparator { _, _ -> 0 } // No sorting applied
)