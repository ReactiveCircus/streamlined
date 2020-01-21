package io.github.reactivecircus.streamlined.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.reactivecircus.analytics.AnalyticsApi
import io.github.reactivecircus.streamlined.home.databinding.FragmentHomeBinding
import io.github.reactivecircus.streamlined.navigator.Navigator
import io.github.reactivecircus.streamlined.ui.base.BaseFragment
import io.github.reactivecircus.streamlined.ui.viewmodel.fragmentViewModel
import javax.inject.Inject
import javax.inject.Provider

class HomeFragment @Inject constructor(
    analyticsApi: AnalyticsApi,
    private val navigator: Navigator,
    private val viewModelProvider: Provider<HomeViewModel>
) : BaseFragment<FragmentHomeBinding>(analyticsApi) {

    private val viewModel: HomeViewModel by fragmentViewModel { viewModelProvider.get() }

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
