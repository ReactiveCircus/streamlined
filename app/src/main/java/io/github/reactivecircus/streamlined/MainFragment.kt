package io.github.reactivecircus.streamlined

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.transition.MaterialElevationScale
import io.github.reactivecircus.streamlined.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        (view.parent as ViewGroup).doOnPreDraw { startPostponedEnterTransition() }
        exitTransition = MaterialElevationScale(false)
        reenterTransition = MaterialElevationScale(true)

        val binding = FragmentMainBinding.bind(view)

        val fragmentContainer = view.findViewById<View>(R.id.mainNavHostFragment)
        val navController = Navigation.findNavController(fragmentContainer)

        // setup NavController with BottomNavigationView
        NavigationUI.setupWithNavController(
            binding.bottomNavigationView,
            navController
        )

        // TODO SDK bug - fragments should NOT be recreated when re-selecting item in bottom navigation view
        //  https://issuetracker.google.com/issues/80029773
    }
}
