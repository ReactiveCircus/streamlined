package io.github.reactivecircus.streamlined.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.github.reactivecircus.streamlined.settings.databinding.FragmentSettingsBinding
import io.github.reactivecircus.streamlined.ui.Screen
import javax.inject.Inject

class SettingsFragment @Inject constructor() : Fragment(R.layout.fragment_settings), Screen {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSettingsBinding.bind(view)

        binding.toolbar.title = getString(R.string.title_settings)
    }
}
