package no.nordicsemi.analytics.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import no.nordicsemi.analytics.NordicAnalytics
import javax.inject.Inject

@HiltViewModel
class AnalyticsPermissionViewModel @Inject constructor(
    private val analytics: NordicAnalytics
) : ViewModel() {

    val permissionData = analytics.permissionData

    fun onConfirmButtonClick() {
        viewModelScope.launch {
            analytics.setAnalyticsEnabled(true)
        }
    }

    fun onDeclineButtonClick() {
        viewModelScope.launch {
            analytics.setAnalyticsEnabled(false)
        }
    }

    fun onPermissionChanged() {
        viewModelScope.launch {
            analytics.switchAnalyticsEnabled()
        }
    }
}
