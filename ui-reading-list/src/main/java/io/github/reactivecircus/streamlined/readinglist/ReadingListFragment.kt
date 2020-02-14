package io.github.reactivecircus.streamlined.readinglist

import android.os.Bundle
import android.view.View
import io.github.reactivecircus.analytics.AnalyticsApi
import io.github.reactivecircus.streamlined.navigator.NavigatorProvider
import io.github.reactivecircus.streamlined.readinglist.databinding.FragmentReadingListBinding
import io.github.reactivecircus.streamlined.ui.base.BaseFragment
import javax.inject.Inject

class ReadingListFragment @Inject constructor(
    analyticsApi: AnalyticsApi,
    private val navigatorProvider: NavigatorProvider
) : BaseFragment(R.layout.fragment_reading_list, analyticsApi) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentReadingListBinding.bind(view)

        binding.toolbar.title = getString(R.string.title_reading_list)
    }
}
