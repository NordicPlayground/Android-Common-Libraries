package no.nordicsemi.android.common.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

val ApplicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
