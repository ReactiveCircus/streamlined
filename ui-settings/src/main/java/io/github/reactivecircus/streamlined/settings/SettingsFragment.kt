package io.github.reactivecircus.streamlined.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import io.github.reactivecircus.streamlined.settings.databinding.FragmentSettingsBinding
import io.github.reactivecircus.streamlined.ui.ScreenForAnalytics

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings), ScreenForAnalytics {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentSettingsBinding.bind(view)

        binding.toolbar.title = getString(R.string.title_settings)
    }
}
