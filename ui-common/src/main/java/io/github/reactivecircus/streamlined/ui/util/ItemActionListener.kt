package io.github.reactivecircus.streamlined.ui.util

fun interface ItemActionListener<A> {
    operator fun invoke(action: A)
}
