package no.nordicsemi.android.common.core

fun ByteArray.toDisplayString(): String {
    return "(0x) " + this.joinToString(":") {
        "%02x".format(it).uppercase()
    }
}
