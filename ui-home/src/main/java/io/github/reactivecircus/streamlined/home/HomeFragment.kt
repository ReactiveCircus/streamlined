package io.github.reactivecircus.streamlined.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.reactivecircus.analytics.AnalyticsApi
import io.github.reactivecircus.streamlined.ui.base.BaseFragment
import io.github.reactivecircus.streamlined.home.databinding.FragmentHomeBinding
import io.github.reactivecircus.streamlined.navigator.Navigator
import javax.inject.Inject

class HomeFragment @Inject constructor(
    analyticsApi: AnalyticsApi,
    private val navigator: Navigator
) : BaseFragment<FragmentHomeBinding>(analyticsApi) {

    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.title = getString(R.string.title_home)
    }
}
