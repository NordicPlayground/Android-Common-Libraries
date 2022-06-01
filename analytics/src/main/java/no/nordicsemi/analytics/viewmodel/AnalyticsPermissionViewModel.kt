package no.nordicsemi.analytics.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import no.nordicsemi.analytics.repository.AnalyticsPermissionRepository
import javax.inject.Inject

@HiltViewModel
class AnalyticsPermissionViewModel @Inject constructor(
    private val repository: AnalyticsPermissionRepository
) : ViewModel() {

    val permissionData = repository.permissionData

    fun onConfirmButtonClick() {
        viewModelScope.launch {
            repository.onPermissionGranted()
        }
    }

    fun onDeclineButtonClick() {
        viewModelScope.launch {
            repository.onPermissionDenied()
        }
    }

    fun onPermissionChanged() {
        viewModelScope.launch {
            permissionData.firstOrNull()?.let {
                if (it.isPermissionGranted) {
                    repository.onPermissionDenied()
                } else {
                    repository.onPermissionGranted()
                }
            }
        }
    }
}