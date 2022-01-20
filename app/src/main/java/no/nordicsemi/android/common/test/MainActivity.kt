package no.nordicsemi.android.common.test

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import no.nordicsemi.android.material.you.NordicTheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NordicTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    FindDeviceScreen() {
//                        when (it) {
//                            FindDeviceCloseResult -> "Flow closed."
//                            is FindDeviceSuccessResult -> it.device.name
//                        }?.let {
//                            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
//                        }
//                    }
                }
            }
        }
    }
}
