package io.github.reactivecircus.streamlined.ui.common.util

fun interface ItemActionListener<A> {
    operator fun invoke(action: A)
}
