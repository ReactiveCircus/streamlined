package io.github.reactivecircus.streamlined.ui.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

/**
 * Collects the [flow] when the [Lifecycle] of the [Fragment] is at least in [Lifecycle.State.STARTED] state,
 * and invokes the given [block] with the collected value [T].
 */
fun <T> Fragment.collectWhenStarted(flow: Flow<T>, block: (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        flow.collect { block(it) }
    }
}
