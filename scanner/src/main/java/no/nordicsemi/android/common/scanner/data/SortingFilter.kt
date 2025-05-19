package no.nordicsemi.android.common.scanner.data

enum class SortType {
    RSSI,
    ALPHABETICAL,;

    override fun toString(): String {
        return when (this) {
            RSSI -> "RSSI"
            ALPHABETICAL -> "Alphabetical"
        }
    }
}