package io.github.reactivecircus.streamlined.ui.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import io.github.reactivecircus.analytics.AnalyticsApi

abstract class BaseFragment(
    @LayoutRes contentLayoutId: Int,
    private val analyticsApi: AnalyticsApi
) : Fragment(contentLayoutId) {

    override fun onResume() {
        super.onResume()
        analyticsApi.setCurrentScreenName(
            requireActivity(),
            javaClass.simpleName,
            javaClass.simpleName
        )
    }
}
