@file:Suppress("unused")

package no.nordicsemi.android.common.navigation.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import no.nordicsemi.android.common.navigation.Navigator
import no.nordicsemi.android.common.navigation.internal.NavigationManager

@Module
@InstallIn(ActivityRetainedComponent::class)
internal interface NavigationModule {

    @Binds
    fun bindNavigator(navigator: NavigationManager): Navigator

}