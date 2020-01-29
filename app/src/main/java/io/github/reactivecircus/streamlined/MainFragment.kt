package io.github.reactivecircus.streamlined

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import io.github.reactivecircus.streamlined.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
