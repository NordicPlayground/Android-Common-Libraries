package no.nordicsemi.android.common.permission.view

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import no.nordicsemi.android.common.permission.PermissionManager
import no.nordicsemi.android.common.permission.PermissionManagerImpl
import javax.inject.Inject

/**
 * Needed for injecting to @Composable functions.
 */
@HiltViewModel
class PermissionViewModel @Inject internal constructor(
    private val permissionManager: PermissionManagerImpl
) : ViewModel(), PermissionManager by permissionManager
