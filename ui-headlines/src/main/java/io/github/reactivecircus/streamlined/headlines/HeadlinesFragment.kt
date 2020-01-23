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
) : BaseFragment(analyticsApi) {

    private val binding get() = view?.tag as FragmentHeadlinesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHeadlinesBinding.inflate(inflater, container, false)
        val view = binding.root
        view.tag = binding
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.title = getString(R.string.title_headlines)
    }
}
