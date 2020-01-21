package io.github.reactivecircus.streamlined.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.reactivecircus.analytics.AnalyticsApi
import io.github.reactivecircus.streamlined.settings.databinding.FragmentSettingsBinding
import io.github.reactivecircus.streamlined.ui.base.BaseFragment
import javax.inject.Inject

class SettingsFragment @Inject constructor(
    analyticsApi: AnalyticsApi
) : BaseFragment<FragmentSettingsBinding>(analyticsApi) {

    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.title = getString(R.string.title_settings)
    }
}
