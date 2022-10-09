package no.nordicsemi.android.common.navigation.internal

import no.nordicsemi.android.common.navigation.Router

internal fun Router.combine(other: Router): Router = { from, hint ->
    this(from, hint) ?: other(from, hint)
}
