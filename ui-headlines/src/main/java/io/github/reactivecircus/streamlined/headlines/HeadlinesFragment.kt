package io.github.reactivecircus.streamlined.headlines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.reactivecircus.analytics.AnalyticsApi
import io.github.reactivecircus.streamlined.headlines.databinding.FragmentHeadlinesBinding
import io.github.reactivecircus.streamlined.navigator.Navigator
import io.github.reactivecircus.streamlined.ui.base.BaseFragment
import javax.inject.Inject

class HeadlinesFragment @Inject constructor(
    analyticsApi: AnalyticsApi,
    private val navigator: Navigator
) : BaseFragment<FragmentHeadlinesBinding>(analyticsApi) {

    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHeadlinesBinding {
        return FragmentHeadlinesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.title = getString(R.string.title_headlines)
    }
}
