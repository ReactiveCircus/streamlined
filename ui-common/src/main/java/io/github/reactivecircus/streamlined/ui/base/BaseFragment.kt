package io.github.reactivecircus.streamlined.ui.base

import androidx.fragment.app.Fragment
import io.github.reactivecircus.analytics.AnalyticsApi

abstract class BaseFragment(
    private val analyticsApi: AnalyticsApi
) : Fragment() {

    override fun onResume() {
        super.onResume()
        analyticsApi.setCurrentScreenName(
            requireActivity(),
            javaClass.simpleName,
            javaClass.simpleName
        )
    }
}
