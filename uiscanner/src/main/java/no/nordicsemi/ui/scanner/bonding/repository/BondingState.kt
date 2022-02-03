package no.nordicsemi.ui.scanner.bonding.repository

import android.bluetooth.BluetoothDevice

enum class BondingState {
    NONE, BONDING, BONDED;

    companion object {
        fun create(value: Int): BondingState {
            return when (value) {
                BluetoothDevice.BOND_BONDED -> BONDED
                BluetoothDevice.BOND_BONDING -> BONDING
                BluetoothDevice.BOND_NONE -> NONE
                else -> throw IllegalArgumentException("Cannot create BondingState for the value: $value")
            }
        }
    }
}

fun BluetoothDevice.getBondingState(): BondingState {
    return when (bondState) {
        BluetoothDevice.BOND_BONDED -> BondingState.BONDED
        BluetoothDevice.BOND_BONDING -> BondingState.BONDING
        else -> BondingState.NONE
    }
}
