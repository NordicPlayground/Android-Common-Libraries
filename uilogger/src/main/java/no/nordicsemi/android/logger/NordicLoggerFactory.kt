package no.nordicsemi.android.logger

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface NordicLoggerFactory {

    fun create(
        @Assisted("appName") appName: String,
        @Assisted("profile") profile: String?,
        @Assisted("key") key: String
    ): NordicLogger
}
