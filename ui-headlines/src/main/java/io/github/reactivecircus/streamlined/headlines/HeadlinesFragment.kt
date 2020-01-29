package io.github.reactivecircus.streamlined.headlines

import android.os.Bundle
import android.view.View
import io.github.reactivecircus.analytics.AnalyticsApi
import io.github.reactivecircus.streamlined.headlines.databinding.FragmentHeadlinesBinding
import io.github.reactivecircus.streamlined.navigator.Navigator
import io.github.reactivecircus.streamlined.ui.base.BaseFragment
import javax.inject.Inject

class HeadlinesFragment @Inject constructor(
    analyticsApi: AnalyticsApi,
    private val navigator: Navigator
) : BaseFragment(R.layout.fragment_headlines, analyticsApi) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHeadlinesBinding.bind(view)

        binding.toolbar.title = getString(R.string.title_headlines)
    }
}
