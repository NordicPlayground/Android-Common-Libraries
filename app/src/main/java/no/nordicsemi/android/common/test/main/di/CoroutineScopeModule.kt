package no.nordicsemi.android.common.test.main.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(SingletonComponent::class)
class CoroutineScopeModule {

    @Provides
    fun applicationScope() = CoroutineScope(SupervisorJob() + Dispatchers.IO)
}