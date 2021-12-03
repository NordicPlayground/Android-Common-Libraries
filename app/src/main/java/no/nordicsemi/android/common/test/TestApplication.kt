package no.nordicsemi.android.common.test

import android.app.Application
import no.nordicsemi.ui.scanner.scannerModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TestApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // declare used Android context
            androidContext(this@TestApplication)
            // declare modules
            modules(scannerModule)
        }
    }
}