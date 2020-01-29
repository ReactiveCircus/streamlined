package io.github.reactivecircus.streamlined.settings

import android.os.Bundle
import android.view.View
import io.github.reactivecircus.analytics.AnalyticsApi
import io.github.reactivecircus.streamlined.settings.databinding.FragmentSettingsBinding
import io.github.reactivecircus.streamlined.ui.base.BaseFragment
import javax.inject.Inject

class SettingsFragment @Inject constructor(
    analyticsApi: AnalyticsApi
) : BaseFragment(R.layout.fragment_settings, analyticsApi) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSettingsBinding.bind(view)

        binding.toolbar.title = getString(R.string.title_settings)
    }
}
