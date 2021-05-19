package io.github.reactivecircus.streamlined.navigator

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import timber.log.Timber

fun Fragment.navigate(
    @IdRes destination: Int,
    args: Parcelable? = null,
    navControllerType: NavControllerType = NavControllerType.Parent,
) = navControllerByType(navControllerType).safeNavigate(destination, createNavArgsBundle(args))

fun <Input : Parcelable> Fragment.requireNavInput(): Input =
    requireArguments().getParcelable(NAV_ARGS_KEY)!!

fun <Input : Parcelable> Fragment.navInputOrNull(): Input? =
    arguments?.getParcelable(NAV_ARGS_KEY)

fun Fragment.navControllerByType(
    navControllerType: NavControllerType = NavControllerType.Parent
): NavController = when (navControllerType) {
    NavControllerType.Root -> requireActivity()
        .findViewById<View>(R.id.rootNavHostFragment)?.findNavController()
        ?: findNavController()
    NavControllerType.Parent -> findNavController()
    is NavControllerType.Child -> {
        val navHostFragment = childFragmentManager
            .findFragmentById(navControllerType.navHostFragmentId) as NavHostFragment
        navHostFragment.navController
    }
}

fun createNavArgsBundle(args: Parcelable?): Bundle? = args?.let {
    bundleOf(NAV_ARGS_KEY to it)
}

private const val NAV_ARGS_KEY = "NAV_ARGS_KEY"

/**
 * Performs a navigation on the [NavController] using the provided [destination] and [args],
 * catching any [IllegalArgumentException] which usually happens when users trigger (e.g. click)
 * navigation multiple times very quickly on slower devices.
 * For more context, see https://stackoverflow.com/questions/51060762/illegalargumentexception-navigation-destination-xxx-is-unknown-to-this-navcontr.
 */
private fun NavController.safeNavigate(destination: Int, args: Bundle?) {
    try {
        navigate(destination, args)
    } catch (e: IllegalArgumentException) {
        Timber.e(e, "Handled navigation destination not found issue gracefully.")
    }
}
