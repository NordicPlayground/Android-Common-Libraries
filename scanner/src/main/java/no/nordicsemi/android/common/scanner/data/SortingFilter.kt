package no.nordicsemi.android.common.scanner.data

/**
 * Represents the type of sorting to be applied to scan results.
 */
enum class SortType {

    /**
     * Sort results by signal strength (RSSI), typically in descending order.
     * Devices with stronger signals appear first.
     */
    RSSI,

    /**
     * Sort results alphabetically by device name.
     * Devices are ordered based on their names in ascending order.
     */
    ALPHABETICAL, ;

    override fun toString(): String {
        return when (this) {
            RSSI -> "RSSI"
            ALPHABETICAL -> "Alphabetical"
        }
    }
}