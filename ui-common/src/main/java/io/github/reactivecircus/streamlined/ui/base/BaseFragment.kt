package io.github.reactivecircus.streamlined.ui.base

import androidx.fragment.app.Fragment
import io.github.reactivecircus.analytics.AnalyticsApi
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

    @Inject
    lateinit var analyticsApi: AnalyticsApi

    override fun onResume() {
        super.onResume()
//        activity?.run {
//            analyticsApi.setCurrentScreenName(this, javaClass.simpleName, javaClass.simpleName)
//        }
    }
}
