package no.nordicsemi.android.common.scanner.data

enum class SortByFilter {
    RSSI,
    NAME_ASCENDING,;

    override fun toString(): String {
        return when (this) {
            RSSI -> "RSSI"
            NAME_ASCENDING -> "Name (Ascending)"
        }
    }
}