package no.nordicsemi.android.common.test

import android.os.Bundle
import android.os.ParcelUuid
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import no.nordicsemi.android.material.you.NordicTheme
import no.nordicsemi.ui.scanner.navigation.view.FindDeviceCloseResult
import no.nordicsemi.ui.scanner.navigation.view.FindDeviceScreen
import no.nordicsemi.ui.scanner.navigation.view.FindDeviceSuccessResult
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NordicTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FindDeviceScreen(uuid = ParcelUuid(UUID.randomUUID())) {
                        when (it) {
                            FindDeviceCloseResult -> "Flow closed."
                            is FindDeviceSuccessResult -> it.device.name
                        }?.let {
                            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}
