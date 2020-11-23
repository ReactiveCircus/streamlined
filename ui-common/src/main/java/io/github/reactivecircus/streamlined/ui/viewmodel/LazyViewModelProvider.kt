package io.github.reactivecircus.streamlined.ui.viewmodel

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Returns a property delegate to access [ViewModel] which is created by invoking the [provider].
 */
inline fun <reified T : ViewModel> Fragment.fragmentViewModel(
    crossinline provider: () -> T
) = viewModels<T> {
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return provider() as T
        }
    }
}
